package com.mhr.mobile.eventbus;

import com.mhr.mobile.api.response.ResponseHistory;

public class TransaksiEvent {
  public final ResponseHistory.Data data;

  public TransaksiEvent(ResponseHistory.Data data) {
    this.data = data;
  }
}
