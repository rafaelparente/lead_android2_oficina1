package com.example.lead_android2_oficina1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IOrdersQueuerListener {

    private LinearLayout clearOrdersLayout;
    private LinearLayout cancelLoadOrdersLayout;
    private ImageButton loadOrdersButton;
    private ListView ordersList;
    private ArrayAdapter<String> ordersAdapter;
    private OrdersQueuer ordersQueuer;
    private int lastOrdersCount = 0;
    private final List<String> orders = new ArrayList<>();
    private static final DecimalFormat df= new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.clearOrdersLayout = findViewById(R.id.clearOrdersLayout);
        this.cancelLoadOrdersLayout = findViewById(R.id.cancelLoadOrdersLayout);
        this.loadOrdersButton = findViewById(R.id.loadOrdersButton);
        this.ordersList = findViewById(R.id.ordersDisplay);
        this.ordersAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                this.orders);
        this.ordersList.setAdapter(this.ordersAdapter);
    }

    @Override
    public void updateOrders(double order) {
        this.orders.add(String.format("Pedido %04d : R$ %s", this.orders.size() + 1, df.format(order)));
        this.ordersAdapter.notifyDataSetChanged();
    }

    @Override
    public void finishOrders() {
        this.loadOrdersButton.setClickable(true);
        this.loadOrdersButton.setVisibility(View.VISIBLE);
        this.cancelLoadOrdersLayout.setVisibility(View.INVISIBLE);
        if (this.orders.size() > 0) {
            this.clearOrdersLayout.setVisibility(View.VISIBLE);
        }
        if (this.orders.size() > this.lastOrdersCount) {
            Toast.makeText(this, "Novos pedidos carregados com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nenhum novo pedido foi encontrado.", Toast.LENGTH_SHORT).show();
        }
        this.lastOrdersCount = this.orders.size();
    }

    @Override
    public void cancelOrders() {
        this.loadOrdersButton.setClickable(true);
        if (this.orders.size() > 0) {
            this.lastOrdersCount = this.orders.size();
            this.clearOrdersLayout.setVisibility(View.VISIBLE);
        }
        Toast.makeText(this, "Carregamento cancelado!", Toast.LENGTH_SHORT).show();
    }

    public void loadOrders(View view) {
        this.loadOrdersButton.setClickable(false);
        this.ordersQueuer = new OrdersQueuer(this);
        this.ordersQueuer.execute();
        this.loadOrdersButton.setVisibility(View.GONE);
        this.cancelLoadOrdersLayout.setVisibility(View.VISIBLE);
        this.clearOrdersLayout.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Carregando pedidos...", Toast.LENGTH_SHORT).show();
    }

    public void cancelLoadOrders(View view) {
        this.ordersQueuer.cancel(true);
        this.loadOrdersButton.setVisibility(View.VISIBLE);
        this.cancelLoadOrdersLayout.setVisibility(View.INVISIBLE);
    }

    public void clearOrders(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Limpar lista de pedidos")
                .setMessage("Deseja realmente excluir todos os pedidos da lista?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    this.orders.clear();
                    this.lastOrdersCount = 0;
                    this.ordersAdapter.notifyDataSetChanged();
                    this.clearOrdersLayout.setVisibility(View.INVISIBLE);
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

}
