"""Test for posts app"""
from django.test import TestCase
from django.contrib.auth.models import User
from django.urls.base import reverse
from rest_framework import status

from .models import Post
from .serializers import PostSerializer


# Create your tests here.
class BasePostTest(TestCase):
    """Base post test class that creates the user for the tests"""

    def __init__(self, *args, **kwargs):
        super(BasePostTest, self).__init__(*args, **kwargs)
        self.user = None

    def setUp(self):
        """Creates the user that will be used in the tests"""
        try:
            self.user = User.objects.get(username='test')
        except User.DoesNotExist:
            self.user = User.objects.create_user(
                email='test@test.com', username='test', password='GreatPassword123'
            )


class PostModelTests(BasePostTest):
    """Test creation of posts"""

    def test_create_post(self):
        """Tests the creation of a normal post"""
        content = 'Hi!'
        post = Post(user=self.user, content=content)
        post.save()
        post = Post.objects.get(user=self.user, content=content)
        self.assertEqual(post.content, content)
        self.assertEqual(post.user.id, self.user.id)

    def test_delete_post_creator(self):
        """Test what happens when a creator of a post is deleted"""
        content = 'Hi!'
        user = User.objects.create_user(
            email='test@test.com', username='test1', password='GreatPassword123'
        )
        post = Post(user=user, content=content)
        post.save()
        user.delete()
        post = Post.objects.get(user=None, content=content)
        self.assertEqual(post.content, content)
        self.assertEqual(post.user, None)


class PostViewsetTest(BasePostTest):
    """Test the API endpoints for the post viewset"""

    def __init__(self, *args, **kwargs):
        super(PostViewsetTest, self).__init__(*args, **kwargs)
        self.token = None

    def setUp(self):
        super(PostViewsetTest, self).setUp()
        auth_response = self.client.post(
            reverse('token-auth'),
            format='json',
            data={
                'username': self.user.username,
                'password': 'GreatPassword123'
            }
        )
        self.token = auth_response.data['token']

    def test_create_post(self):
        """Tests the creation of a post"""
        content = 'Hi!!'
        response = self.client.post(
            reverse('post-list'),
            format='json',
            data={
                'content': content,
                'user': self.user.id
            },
            HTTP_AUTHORIZATION='Token %s' % self.token
        )
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Post.objects.get().content, content)

    def test_create_empty_post(self):
        """Tests the creation of an empty post"""
        response = self.client.post(
            reverse('post-list'),
            format='json',
            data={
                'content': '',
                'user': self.user.id
            },
            HTTP_AUTHORIZATION='Token %s' % self.token
        )
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertEqual(response.data['content'][0], 'This field may not be blank.')

    def test_empty_post_list(self):
        """Tests the listing of posts when no posts are created"""
        response = self.client.get(reverse('post-list'))
        self.assertEqual(response.data, [])

    def test_list_posts(self):
        """Tests the listing of posts"""
        user = User.objects.create_user(
            email='test@test.com', username='test2', password='GreatPassword123'
        )
        self.client.post(
            reverse('post-list'),
            format='json',
            data={
                'content': 'Hi this is a test',
                'user': self.user.id
            },
            HTTP_AUTHORIZATION='Token %s' % self.token
        )
        self.client.post(
            reverse('post-list'),
            format='json',
            data={
                # Translation: This test has weird characters!
                'content': '¡¡Éste test tiene caractéres rarós!!',
                'user': user.id
            },
            HTTP_AUTHORIZATION='Token %s' % self.token
        )
        user.delete()
        response = self.client.get(reverse('post-list'))
        serializer = PostSerializer(Post.objects.all(), many=True)
        self.assertEqual(response.data, serializer.data)
