package com.digidot.ishansupportsystem.retrofit;

import com.digidot.ishansupportsystem.model.BroadResponse;
import com.digidot.ishansupportsystem.model.CityResponse;
import com.digidot.ishansupportsystem.model.ClientResponse;
import com.digidot.ishansupportsystem.model.DependencyResponse;
import com.digidot.ishansupportsystem.model.EndPoint;
import com.digidot.ishansupportsystem.model.FaultResponse;
import com.digidot.ishansupportsystem.model.LoginResponse;
import com.digidot.ishansupportsystem.model.NotificationResponse;
import com.digidot.ishansupportsystem.model.OfficeResponse;
import com.digidot.ishansupportsystem.model.ResolutionResponse;
import com.digidot.ishansupportsystem.model.StateResponse;
import com.digidot.ishansupportsystem.model.TicketResponse;
import com.digidot.ishansupportsystem.model.UpdateTicketResponce;
import com.digidot.ishansupportsystem.model.ZoneResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by ABC on 25-01-2018.
 */

public interface APIService {

    @POST("Login")
    @FormUrlEncoded
    Call<LoginResponse> login(@FieldMap Map<String, String> loginRequest);

    @POST("GetProject")
    @FormUrlEncoded
    Call<EndPoint> getEndPoint(@Field("ProjectName") String ProjectName);

    @POST("State")
    @FormUrlEncoded
    Call<StateResponse> getState(@FieldMap Map<String, String> stateRequest);

    @POST("City")
    @FormUrlEncoded
    Call<CityResponse> getCity(@FieldMap Map<String, String> cityRequest);

    @POST("Zone")
    @FormUrlEncoded
    Call<ZoneResponse> getZone(@FieldMap Map<String, String> zoneRequest);

    @POST("Client")
    @FormUrlEncoded
    Call<ClientResponse> getClient(@FieldMap Map<String, String> clientRequest);

    @POST("Office")
    @FormUrlEncoded
    Call<OfficeResponse> getOffice(@FieldMap Map<String, String> officeRequest);

    @POST("Dependency")
    @FormUrlEncoded
    Call<DependencyResponse> getDependency(@FieldMap Map<String, String> dependencyRequest);

    @POST("Fault")
    @FormUrlEncoded
    Call<FaultResponse> getFault(@FieldMap Map<String, String> faultRequest);

    @POST("BroadCategory")
    @FormUrlEncoded
    Call<BroadResponse> getBroad(@FieldMap Map<String, String> broadRequest);

    @POST("Resolution")
    @FormUrlEncoded
    Call<ResolutionResponse> getResolution(@FieldMap Map<String, String> resolutionRequest);

    @POST("CreateTicket")
    @FormUrlEncoded
    Call<Void> createTicket(@FieldMap Map<String, String> createTicketRequest);

    @POST("ViewTicket")
    @FormUrlEncoded
    Call<TicketResponse> getTickets(@FieldMap Map<String, String> ticketRequest);

    @POST("Notification")
    @FormUrlEncoded
    Call<NotificationResponse> getNotifications(@FieldMap Map<String, String> notificationRequest);

    @POST("ViewTicketHistory")
    @FormUrlEncoded
    Call<TicketResponse> getTicketHistory(@FieldMap Map<String, String> ticketHistoryRequest);

    @POST("UpdateTicket")
    @FormUrlEncoded
    Call<UpdateTicketResponce> getUpdateTicket(@FieldMap Map<String, String> updateTicketRequest);
}
