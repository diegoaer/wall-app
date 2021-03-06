"""wall URL Configuration"""
from django.urls import path, include, re_path
from rest_framework.routers import DefaultRouter
from rest_framework.authtoken import views as token_views

from posts import views as post_views
from users import views as user_views

ROUTER = DefaultRouter()
ROUTER.register(r'posts', post_views.PostViewSet)
ROUTER.register(r'users', user_views.UserViewSet)

urlpatterns = [
    path('', include(ROUTER.urls)),
    re_path(r'^auth/', token_views.obtain_auth_token, name='token-auth'),
    path('api-auth/', include('rest_framework.urls')),
]
