from django.db import models
# ..members import models as Mmodel
import members.models as Mmodel


# Create your models here.


class Language_rehabilitation(models.Model):
    user_id = models.ForeignKey(Mmodel.Members, db_column="user_id", on_delete=models.CASCADE)
    language_score = models.IntegerField(db_column="language_score")
    rehabilitation_time = models.DateTimeField(auto_now_add=True, db_column="rehabilitation_time")
    idx = models.AutoField(primary_key=True)
    #language_idx = models.IntegerField(db_column="language_idx")

    class Meta:
        db_table = "language_rehabilitation"



class Physical_rehabilitation(models.Model):
    user_id = models.ForeignKey(Mmodel.Members, db_column="user_id", on_delete=models.CASCADE)
    physical_score = models.IntegerField(db_column="physical_score")
    rehabilitation_time = models.DateTimeField(auto_now_add=True, db_column="rehabilitation_time")
    idx = models.AutoField(primary_key=True)
    #physical_idx = models.IntegerField(db_column="physical_idx")

    class Meta:
        db_table = "physical_rehabilitation"



