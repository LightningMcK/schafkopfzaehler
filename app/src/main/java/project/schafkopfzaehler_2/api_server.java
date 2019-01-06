package project.schafkopfzaehler_2;

import okhttp3.MultipartBody;
import project.schafkopfzaehler_2.POJO.POJOClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface api_server{

        @Multipart
        @POST("/face_detection/detect/")

        Call<POJOClass> uploadAttachment(@Part MultipartBody.Part filePart);

        void getData(Callback<POJOClass> response);

}
