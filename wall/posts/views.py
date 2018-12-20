"""Views for the posts app"""
from rest_framework.viewsets import GenericViewSet, mixins
from rest_framework import permissions

from .models import Post
from .serializers import PostSerializer


# Create your views here.
class PostViewSet(mixins.CreateModelMixin, mixins.ListModelMixin, GenericViewSet):
    """Enpoints for creation and listing of Posts"""
    queryset = Post.objects.all()
    serializer_class = PostSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly,)
