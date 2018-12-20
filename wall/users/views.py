"""Views for the users app"""
from rest_framework.viewsets import GenericViewSet, mixins
from django.core.mail import send_mail
from django.contrib.auth.models import User

from .serializers import UserSerializer


# Create your views here.
class UserViewSet(mixins.CreateModelMixin, GenericViewSet):
    """Enpoint for user creation"""
    queryset = User.objects.none()
    serializer_class = UserSerializer

    def perform_create(self, serializer):
        """Overrides the defualt ViewSet create to send an email on user creation"""
        super(UserViewSet, self).perform_create(serializer)
        send_mail(
            'Welcome',
            'This is  welcome email that would be way better in a real app!',
            'contact@wallapp.com', [serializer.instance.email],
            fail_silently=False
        )
