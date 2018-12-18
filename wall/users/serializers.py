"""Serializers for users app"""
from django.contrib.auth.password_validation import validate_password
from rest_framework import serializers

from django.contrib.auth.models import User


class UserSerializer(serializers.ModelSerializer):
    """Serializer for the user model"""

    class Meta:
        model = User
        fields = ('username', 'email', 'password')

    def validate(self, attrs):
        """
        Validates the password again with django validators, usefull for validators that do not 
        depend only on the password
        """
        dummy_user = User(**attrs)
        validate_password(attrs['password'], user=dummy_user)
        return attrs

    def validate_password(self, value):
        """Validates the password with django validators"""
        validate_password(value)
        return value

    def create(self, validated_data):
        instance = super(UserSerializer, self).create(validated_data)
        instance.set_password(validated_data['password'])
        instance.save()
        return instance

    def to_representation(self, instance):
        ret = super(UserSerializer, self).to_representation(instance)
        del ret['password']
        return ret
