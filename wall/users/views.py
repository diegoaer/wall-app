"""Views for the users app"""
from rest_framework.viewsets import GenericViewSet, mixins

from django.contrib.auth.models import User
from .serializers import UserSerializer


# Create your views here.
class UserViewSet(mixins.CreateModelMixin, GenericViewSet):
    """Enpoint for user creation"""
    queryset = User.objects.none()
    serializer_class = UserSerializer
