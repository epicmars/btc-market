package com.fafabtc.app.ui.viewholder;

import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ViewHolderTickerBinding;
import com.fafabtc.app.ui.TradeActivity;
import com.fafabtc.app.ui.base.BaseViewHolder;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.data.model.entity.exchange.Ticker;

/**
 * Created by jastrelax on 2018/1/7.
 */
@BindLayout(value = R.layout.view_holder_ticker, dataTypes = Ticker.class)
public class TickerViewHolder extends BaseViewHolder<ViewHolderTickerBinding>{

    public TickerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBindView(T data, int position) {
        if (data == null)
            return;
        final Ticker ticker = (Ticker) data;

        double percent = ticker.getPercentChange();
        int percentBackground = percent < 0 ? R.drawable.bg_tv_percent_negative : R.drawable.bg_tv_percent_positive;
        mBinding.tvPercentChange.setBackground(itemView.getResources().getDrawable(percentBackground));
//        mBinding.tvPercentChange.setText(NumberUtils.formatPercent(ticker.getPercentChange()/100));
//
//        mBinding.tvBase.setText(ticker.getBase().toUpperCase());
//        mBinding.tvQuote.setText(itemView.getResources().getString(R.string.ticker_quote_name_format, ticker.getQuote().toUpperCase()));
//        mBinding.tvLast.setText(NumberUtils.formatPrice(ticker.getLast()));
//        mBinding.tvBaseVolume.setText(itemView.getResources().getString(R.string.ticker_base_volume_format, NumberUtils.formatVolumeCN(ticker.getBaseVolume())));
//        mBinding.tvQuoteVolume.setText(itemView.getResources().getString(R.string.ticker_quote_volume_format, ticker.getQuote().toUpperCase(), NumberUtils.formatVolumeCN(ticker.getQuoteVolume())));
        mBinding.setTicker(ticker);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeActivity.start(v.getContext(), ticker);
            }
        });
        mBinding.executePendingBindings();
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        itemView.setOnClickListener(null);
    }
}
