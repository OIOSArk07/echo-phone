package com.oiosark07.echowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public final class EchoWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_TOGGLE = "com.oiosark07.echowidget.TOGGLE_REST";
    private static final String PREFERENCES = "echo_widget";
    private static final String SLEEPING = "sleeping";

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) update(context, manager, id);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (!ACTION_TOGGLE.equals(intent.getAction())) return;
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(SLEEPING, !preferences.getBoolean(SLEEPING, false)).apply();
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, EchoWidgetProvider.class));
        for (int id : ids) update(context, manager, id);
    }

    private static void update(Context context, AppWidgetManager manager, int widgetId) {
        boolean sleeping = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
                .getBoolean(SLEEPING, false);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.echo_widget);
        views.setImageViewResource(
                R.id.echo_owl,
                sleeping ? R.drawable.echo_widget_sleeping : R.drawable.echo_widget_awake
        );
        views.setTextViewText(R.id.rest_toggle, sleeping ? "Wake" : "Rest");

        Intent openIntent = new Intent(context, OpenEchoActivity.class);
        PendingIntent open = PendingIntent.getActivity(
                context, 1, openIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.echo_owl, open);

        Intent toggleIntent = new Intent(context, EchoWidgetProvider.class).setAction(ACTION_TOGGLE);
        PendingIntent toggle = PendingIntent.getBroadcast(
                context, 2, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.rest_toggle, toggle);
        manager.updateAppWidget(widgetId, views);
    }
}
