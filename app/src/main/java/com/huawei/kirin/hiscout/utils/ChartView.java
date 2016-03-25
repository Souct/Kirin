package com.huawei.kirin.hiscout.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.huawei.kirin.hiscout.MainView;
import com.huawei.kirin.hiscout.PMPActivity;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by spock on 3/24/16.
 */
public class ChartView {
    private MainView mMainView;
    private PMPActivity mActivity;
    private XYSeries mLine;
    private XYMultipleSeriesDataset mDataSet; // 用于存放所有需要绘制的XYSeries
    private XYSeriesRenderer mRenderer;// 用于存放每条折线的风格
    private XYMultipleSeriesRenderer mXYMultipleSeriesRenderer;// 用于存放所有需要绘制的折线的风格
    private GraphicalView mGraphicalView;
    private RelativeLayout mLineChartView;

    private double[] x, y;
    private int count;
    private double xTemp,yTemp;

    public ChartView(MainView mainView, RelativeLayout LineChartView) {
        mMainView = mainView;
        mActivity = mainView.getActivity();
        mLineChartView = LineChartView;

    }

    public void initChart() {
        // 初始化，必须保证XYMultipleSeriesDataset对象中的XYSeries数量和
        // XYMultipleSeriesRenderer对象中的XYSeriesRenderer数量一样多
        mLine = new XYSeries("折线1");
        mRenderer = new XYSeriesRenderer();
        mDataSet = new XYMultipleSeriesDataset();
        mXYMultipleSeriesRenderer = new XYMultipleSeriesRenderer();

        // 对XYSeries和XYSeriesRenderer的对象的参数赋值
        initRenderer(mRenderer, Color.GREEN, PointStyle.POINT, true);

        // 将XYSeries对象和XYSeriesRenderer对象分别添加到XYMultipleSeriesDataset对象和XYMultipleSeriesRenderer对象中。
        mDataSet.addSeries(mLine);
        mXYMultipleSeriesRenderer.addSeriesRenderer(mRenderer);

        // 配置chart参数
        setChartSettings(mXYMultipleSeriesRenderer, "X", "Y", 0, 20, 0, 500, Color.RED,
                Color.WHITE);

        mGraphicalView = ChartFactory.getLineChartView(mActivity, mDataSet, mXYMultipleSeriesRenderer);
        mLineChartView.addView(mGraphicalView);
        x = new double[20];
        y = new double[20];
    }

    private XYSeriesRenderer initRenderer(XYSeriesRenderer renderer, int color,
                                          PointStyle style, boolean fill) {
        // 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        renderer.setColor(color);
        renderer.setPointStyle(style);
        renderer.setFillPoints(fill);
        renderer.setLineWidth(1);
        return renderer;
    }
    private void setChartSettings(XYMultipleSeriesRenderer mXYMultipleSeriesRenderer,
                                    String xTitle, String yTitle, double xMin, double xMax,
                                    double yMin, double yMax, int axesColor, int labelsColor) {
        // 有关对图表的渲染可参看api文档
        mXYMultipleSeriesRenderer.setChartTitle("test");
        mXYMultipleSeriesRenderer.setXTitle("time");
        mXYMultipleSeriesRenderer.setYTitle("hz");
        mXYMultipleSeriesRenderer.setXAxisMin(xMin);
        mXYMultipleSeriesRenderer.setAxisTitleTextSize(30);
        mXYMultipleSeriesRenderer.setChartTitleTextSize(50);
        mXYMultipleSeriesRenderer.setLabelsTextSize(15);
        mXYMultipleSeriesRenderer.setXAxisMax(xMax);
        mXYMultipleSeriesRenderer.setYAxisMin(yMin);
        mXYMultipleSeriesRenderer.setYAxisMax(yMax);
        mXYMultipleSeriesRenderer.setAxesColor(axesColor);
        mXYMultipleSeriesRenderer.setLabelsColor(labelsColor);
        mXYMultipleSeriesRenderer.setShowGrid(false);
        mXYMultipleSeriesRenderer.setXLabels(20);
        mXYMultipleSeriesRenderer.setYLabels(10);

        mXYMultipleSeriesRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mXYMultipleSeriesRenderer.setPointSize((float) 5);
        mXYMultipleSeriesRenderer.setShowLegend(true);
        mXYMultipleSeriesRenderer.setLegendTextSize(20);
        mXYMultipleSeriesRenderer.setClickEnabled(false);
        mXYMultipleSeriesRenderer.setZoomEnabled(false);
        mXYMultipleSeriesRenderer.setInScroll(false);
    }
    public void RefreshSeriesTask(int yValue){
        initLine(mLine,yValue);
        mGraphicalView.postInvalidate();
    }

    private void initLine(XYSeries mLine, int yValue) {
        xTemp = 0;
        yTemp = yValue;

        count = mLine.getItemCount();
        if (count > 20) {
            count = 20;
        }

        for (int i = 0; i < count; i++) {
            x[i] = mLine.getX(i);
            y[i] = mLine.getY(i);
        }
        mLine.clear();

        mLine.add(xTemp, yTemp);

        for (int i = 0; i < count; i++) {
            mLine.add(x[i] + 1, y[i]);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("dataset", mDataSet);
        outState.putSerializable("renderer", mXYMultipleSeriesRenderer);
        outState.putSerializable("current_series", mLine);
        outState.putSerializable("current_renderer", mRenderer);
    }

    public void onRestoreInstanceState(Bundle savedState) {
        mDataSet = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
        mXYMultipleSeriesRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
        mLine = (XYSeries) savedState.getSerializable("current_series");
        mRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");
    }
}
