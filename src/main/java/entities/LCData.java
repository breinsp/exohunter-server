package entities;

import java.util.List;

public class LCData {
    private int epicNr;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    private List<Double> xAxis;
    private List<Double> yAxis;

    public LCData(int epicNr, double xMin, double xMax, double yMin, double yMax, List<Double> xAxis, List<Double> yAxis) {
        this.epicNr = epicNr;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public int getEpicNr() {
        return epicNr;
    }

    public void setEpicNr(int epicNr) {
        this.epicNr = epicNr;
    }

    public double getxMin() {
        return xMin;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public double getxMax() {
        return xMax;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public double getyMin() {
        return yMin;
    }

    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    public List<Double> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<Double> xAxis) {
        this.xAxis = xAxis;
    }

    public List<Double> getyAxis() {
        return yAxis;
    }

    public void setyAxis(List<Double> yAxis) {
        this.yAxis = yAxis;
    }
}
