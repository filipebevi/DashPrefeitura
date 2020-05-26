/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.dashprefeitura.expense;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Filipe Bevilaqua
 */
@ManagedBean
@RequestScoped
public class ExpenseBean {

    private BarChartModel barModel;
    private List<Expense> listaMes;

    
    private BarChartModel barModelCategory;
    private List<Expense> listaCat;
    
    private BarChartModel barModelSource;
    private List<Expense> listaSource;
    
    ExpenseAPI expenseAPI;

   
    public ExpenseBean() {

        try {
            this.expenseAPI = new ExpenseAPI();
        } catch (IOException ex) {
            Logger.getLogger(ExpenseBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public BarChartModel getBarModel() {

        Map<Integer, Double> sum = expenseAPI.listarPorMes();
        barModel = new BarChartModel();
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Valor");
        for (Map.Entry<Integer, Double> entry : sum.entrySet()) {
            Integer key = entry.getKey();
            Double value = entry.getValue();
            DecimalFormat df = new DecimalFormat("0.00");
            boys.set(key, value);
        }
        barModel.setTitle("Por Mês");
        barModel.setLegendPosition("ne");
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Mês");
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Valor");
        barModel.addSeries(boys);

        return barModel;
    }

    public BarChartModel getBarModelCategory() {
        Map<String, Double> sum = expenseAPI.listarPorCategoria();
        barModelCategory = new BarChartModel();
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Valor");
        for (Map.Entry<String, Double> entry : sum.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            DecimalFormat df = new DecimalFormat("0.00");
            boys.set(key, value);
        }
        barModelCategory.setTitle("Por Categoria");
        barModelCategory.setLegendPosition("ne");
        Axis xAxis = barModelCategory.getAxis(AxisType.X);
        xAxis.setLabel("Categoria");
        Axis yAxis = barModelCategory.getAxis(AxisType.Y);
        yAxis.setLabel("Valor");
        barModelCategory.addSeries(boys);

        return barModelCategory;
    }
    
    public BarChartModel getBarModelSouce() {
        Map<String, Double> sum = expenseAPI.listarPorOrigem();
        barModelSource = new BarChartModel();
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Valor");
        for (Map.Entry<String, Double> entry : sum.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            DecimalFormat df = new DecimalFormat("0.00");
            boys.set(key, value);
        }
        barModelSource.setTitle("Por Origem");
        barModelSource.setLegendPosition("ne");
        Axis xAxis = barModelSource.getAxis(AxisType.X);
        xAxis.setLabel("Categoria");
        Axis yAxis = barModelSource.getAxis(AxisType.Y);
        yAxis.setLabel("Valor");
        barModelSource.addSeries(boys);

        return barModelSource;
    }
    
    public List<Expense> getListaMes() {
        listaMes = new ArrayList();
         Map<Integer, Double> sum = expenseAPI.listarPorMes();
         
         for (Map.Entry<Integer, Double> entry : sum.entrySet()) {
            Integer key = entry.getKey();
            Double value = entry.getValue();
            DecimalFormat df = new DecimalFormat("0.00");
            Expense expense = new Expense();
            expense.setMes(key);
            expense.setValor(value);
            listaMes.add(expense);
            //System.out.println("Key: "+key.toString()+" | "+df.format(value));
        }
        
        
        return listaMes;
    }
    
    public List<Expense> getListaCat() {
        listaCat = new ArrayList();
         Map<String, Double> sum = expenseAPI.listarPorCategoria();
         
         for (Map.Entry<String, Double> entry : sum.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            DecimalFormat df = new DecimalFormat("0.00");
            Expense expense = new Expense();
            expense.setCategory(key);
            expense.setValor(value);
            listaCat.add(expense);
            //System.out.println("Key: "+key.toString()+" | "+df.format(value));
        }
        
        
        return listaCat;
    }
    
    public List<Expense> getListaSource() {
        listaSource = new ArrayList();
         Map<String, Double> sum = expenseAPI.listarPorOrigem();
         
         for (Map.Entry<String, Double> entry : sum.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            DecimalFormat df = new DecimalFormat("0.00");
            Expense expense = new Expense();
            expense.setSource(key);
            expense.setValor(value);
            listaSource.add(expense);
            //System.out.println("Key: "+key.toString()+" | "+df.format(value));
        }
        
        
        return listaSource;
    }

}
