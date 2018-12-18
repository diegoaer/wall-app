"""Serializers for posts app"""
from rest_framework import serializers

from .models import Post


class PostSerializer(serializers.ModelSerializer):
    """Serializer for the post model"""

    class Meta:
        model = Post
        fields = ('content', 'user')
