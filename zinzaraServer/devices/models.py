from django.db import models
#from members import models as Mmodel
import members.models as Mmodel

# Create your models here.


class Devices(models.Model):
    user_id = models.ForeignKey(Mmodel.Members, db_column="user_id", on_delete=models.CASCADE)
    device_name = models.CharField(max_length=20, db_column="device_name")
    device_command = models.CharField(max_length=10, db_column="device_command")
    device_command_time = models.DateTimeField(auto_now_add=True, db_column="device_command_time")
    idx = models.AutoField(primary_key=True)

    class Meta:
        db_table = "devices"

