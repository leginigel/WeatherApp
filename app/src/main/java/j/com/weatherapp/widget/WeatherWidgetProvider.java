package j.com.weatherapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import j.com.weatherapp.MainActivity;
import j.com.weatherapp.R;

public class WeatherWidgetProvider extends AppWidgetProvider {
    private static final String Tag = "WeatherWidgetProvider";

//    public WeatherWidgetProvider() {
//        Log.d(this.getClass().getSimpleName(), "onCreate");
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(this.getClass().getSimpleName(), "onUpdate");
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
            view.setTextViewText(R.id.widget_text, "Mother");
            view.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appId, view);
        }

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.d(Tag, "onAppWidgetOptionsChanged");
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(Tag, "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(Tag, "onDisabled");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(this.getClass().getSimpleName(), "onDeleted");
    }
}
