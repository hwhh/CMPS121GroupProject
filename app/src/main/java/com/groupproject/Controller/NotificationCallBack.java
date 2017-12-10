package com.groupproject.Controller;

import com.groupproject.Model.Notification;

import java.util.List;

public interface NotificationCallBack {
    void getNotifications(List<Notification> notifications);
}
