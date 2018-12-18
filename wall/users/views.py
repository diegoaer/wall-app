"""Views for the users app"""
from rest_framework.viewsets import GenericViewSet, mixins

from .serializers import UserSerializer


# Create your views here.
class UserViewSet(mixins.CreateModelMixin, GenericViewSet):
    """Enpoint for user creation"""
    serializer_class = UserSerializer
