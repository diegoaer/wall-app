"""Serializers for posts app"""
from rest_framework import serializers

from .models import Post


class PostSerializer(serializers.ModelSerializer):
    """Serializer for the post model"""

    class Meta:
        model = Post
        fields = ('id', 'content', 'user', 'date')

    def validate_user(self, value):
        """If the user is none set the current user as poster"""
        if value is None:
            return self.context['request'].user
        return value

    def to_representation(self, instance):
        """Returns the username instead of the user id"""
        ret = super(PostSerializer, self).to_representation(instance)
        if instance.user:
            ret['user'] = instance.user.username
        return ret
