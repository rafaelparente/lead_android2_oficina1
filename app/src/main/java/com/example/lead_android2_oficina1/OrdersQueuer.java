package com.example.lead_android2_oficina1;

import android.content.Context;
import android.os.AsyncTask;

import java.util.concurrent.ThreadLocalRandom;

public class OrdersQueuer extends AsyncTask<Integer, Double, Void> {

    private final IOrdersQueuerListener listener;

    public OrdersQueuer(Context context) {
        this.listener = (IOrdersQueuerListener) context;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int ordersCount = ThreadLocalRandom.current().nextInt(0, 11);
        try {
            for (int i = 0; i < ordersCount; ++i) {
                if (isCancelled()) {
                    return null;
                }
                double order = OrderFetcher.getOrder();
                publishProgress(order);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);
        this.listener.updateOrders(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.listener.finishOrders();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        this.listener.cancelOrders();
    }
}
