"""Serializers for users app"""
from rest_framework import serializers

from django.contrib.auth.models import User


class UserSerializer(serializers.ModelSerializer):
    """Serializer for the user model"""

    class Meta:
        model = User
        fields = ('username', 'email', 'password')

    def create(self, validated_data):
        instance = super(UserSerializer, self).create(validated_data)
        instance.set_password(validated_data['password'])
        return instance