"""Post module models"""
from django.db import models
from django.contrib.auth.models import User


# Create your models here.
class Post(models.Model):
    """A wall post"""
    content = models.TextField()
    user = models.ForeignKey(User, null=True, blank=False, on_delete=models.SET_NULL)
    date = models.DateTimeField(auto_now_add=True)
