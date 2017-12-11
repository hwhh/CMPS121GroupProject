package com.groupproject.Controller;

import com.groupproject.Model.PinnedNotification;

import java.util.List;

public interface NotificationCallBack {
    void getNotifications(List<PinnedNotification> notifications);
}
