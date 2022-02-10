from django.db import models

# Create your models here.
# MODEL은 데이터를 관리하는 부분


class Members(models.Model):
    user_id = models.CharField(primary_key=True, max_length=30, db_column="user_id")
    pw = models.CharField(max_length=15, db_column="pw")
    phone_number = models.CharField(max_length=13, db_column="phone_number")
    created = models.DateTimeField(auto_now_add=True, db_column="created")

    class Meta:
        ordering = ["created"]
        db_table = "members"