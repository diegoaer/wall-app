"""Tests for user app"""
from django.test import TestCase
from django.contrib.auth.models import User
from django.contrib.auth import authenticate
from django.urls.base import reverse
from rest_framework.test import RequestsClient
from rest_framework import status


# Create your tests here.
class PostViewsetTest(TestCase):
    """Post create API endpoint tests"""

    def __init__(self, *args, **kwargs):
        super(PostViewsetTest, self).__init__(*args, **kwargs)
        self.client = RequestsClient()

    def test_user_creation(self):
        """Tests the creation of a user"""
        username = 'test'
        password = 'SuchAnwesomePassword'
        response = self.client.post(
            reverse('user-list'),
            format='json',
            data={
                'username': username,
                'password': password,
                'email': 'test@guerrillamail.com'  # see emails in guerrillamail.com
            }
        )
        user = User.objects.get(username=username)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(user, authenticate(username=username, password=password))

    def test_user_empty_password(self):
        """Tests the creation of a user without password"""
        username = 'test'
        response = self.client.post(
            reverse('user-list'),
            format='json',
            data={
                'username': username,
                'password': '',
                'email': 'test@guerrillamail.com'  # see emails in guerrillamail.com
            }
        )
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertEqual(response.data['password'][0], 'This field may not be blank.')

    def test_user_short_password(self):
        """Tests the creation of a user with a short password"""
        response = self.client.post(
            reverse('user-list'),
            format='json',
            data={
                'username': 'test',
                'password': 'a',
                'email': 'test@guerrillamail.com'  # see emails in guerrillamail.com
            }
        )
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertEqual(
            response.data['password'][0],
            'This password is too short. It must contain at least 8 characters.'
        )

    def test_user_common_password(self):
        """Tests the creation of a user with a common password"""
        response = self.client.post(
            reverse('user-list'),
            format='json',
            data={
                'username': 'test',
                'password': 'admin123',
                'email': 'test@guerrillamail.com'  # see emails in guerrillamail.com
            }
        )
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertEqual(response.data['password'][0], 'This password is too common.')

    def test_user_numeric_password(self):
        """Tests the creation of a user with a numeric only password"""
        response = self.client.post(
            reverse('user-list'),
            format='json',
            data={
                'username': 'test',
                'password': '56482852197',
                'email': 'test@guerrillamail.com'  # see emails in guerrillamail.com
            }
        )
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertEqual(response.data['password'][0], 'This password is entirely numeric.')

    def test_user_similar_password(self):
        """Tests the creation of a user with a password similar to the username"""
        response = self.client.post(
            reverse('user-list'),
            format='json',
            data={
                'username': 'theBestUserName',
                'password': 'theBestUserName',
                'email': 'test@guerrillamail.com'  # see emails in guerrillamail.com
            }
        )
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertEqual(
            response.data['non_field_errors'][0], 'The password is too similar to the username.'
        )
