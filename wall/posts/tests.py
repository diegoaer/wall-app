"""Test for posts app"""
from django.test import TestCase
from django.contrib.auth.models import User

from .models import Post


# Create your tests here.
class PostModelTests(TestCase):
    """Test creation of posts"""

    def __init__(self, *args, **kwargs):
        super(PostModelTests, self).__init__(*args, **kwargs)
        self.user = None

    def setup(self):
        """Creates the user that will be used in the tests"""
        self.user = User.objects.create_user(
            email='test@test.com', username='test', password='GreatPassword123'
        )

    def test_create_post(self):
        """Tests the creation of a normal post"""
        content = 'Hi!'
        post = Post(user=self.user, content=content)
        post.save()
        post = Post.objects.get(user=self.user, content=content)
        self.assertEqual(post.content, content)
        self.assertEqual(post.user, self.user)

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
