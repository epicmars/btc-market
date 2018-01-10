package com.fafabtc.app.vm;

import android.arch.lifecycle.ViewModel;

import com.fafabtc.gateio.model.entity.GateioTicker;

import javax.inject.Inject;

/**
 * Created by jastrelax on 2018/1/9.
 */

public class GateioTradeViewModel extends ViewModel{

    private GateioTicker gateioTicker;

    @Inject
    public GateioTradeViewModel() {
    }

    public void setGateioTicker(GateioTicker gateioTicker) {
        this.gateioTicker = gateioTicker;
    }
}
