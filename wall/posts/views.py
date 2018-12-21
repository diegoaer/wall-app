"""Views for the posts app"""
from rest_framework.viewsets import GenericViewSet, mixins
from rest_framework import permissions

from .models import Post
from .serializers import PostSerializer


# Create your views here.
class PostViewSet(mixins.CreateModelMixin, mixins.ListModelMixin, GenericViewSet):
    """Enpoints for creation and listing of Posts"""
    queryset = Post.objects.all().order_by('id')
    serializer_class = PostSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly,)

    def get_queryset(self):
        """If the since url parameter exist, filter the queryset"""
        queryset = super(PostViewSet, self).get_queryset()
        since = self.request.query_params.get('since', None)
        if since:
            return queryset.filter(id__gt=since)
        return queryset
