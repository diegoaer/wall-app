"""Serializers for users app"""
from rest_framework import serializers

from django.contrib.auth.models import User


class PostSerializer(serializers.ModelSerializer):
    """Serializer for the user model"""

    class Meta:
        model = User
        fields = ('username', 'email', 'pasword')
