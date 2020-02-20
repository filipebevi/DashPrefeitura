/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cesarchallenger.expense;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Filipe Bevilaqua
 */
public class ExpenseAPI {

    List<Expense> expenses = new ArrayList();

    public ExpenseAPI() throws IOException {
        this.expenses = iniciar();

    }

    //Metodo para ler o buffer do aquivo
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    //metodo para receber o json
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /*
        metodo para iniciar o json atraves da url;
        A ideia deste método é receber o json e iniciar o tratamento
        pegando as informações das tags selecionas e jogando dentro de uma Lista
     */
    public List<Expense> iniciar() throws IOException {
        //devido a quantidade de dados limitei a pesquisa para 1000 resultados.
        JSONObject json = readJsonFromUrl("http://dados.recife.pe.gov.br/api/3/action/datastore_search?limit=100&resource_id=d4d8a7f0-d4be-4397-b950-f0c991438111");
        //System.out.println(json.toString());

        int tamanho = json.getJSONObject("result")
                .getJSONArray("records").length();

        List<Expense> expenses = new ArrayList();

        for (int i = 0; i < tamanho; i++) {
            Expense expense = new Expense();
            expense.setCategory(json.getJSONObject("result")
                    .getJSONArray("records")
                    .getJSONObject(i)
                    .getString("categoria_economica_nome"));

            expense.setMes(json.getJSONObject("result")
                    .getJSONArray("records")
                    .getJSONObject(i)
                    .getInt("mes_movimentacao"));

            expense.setSource(json.getJSONObject("result")
                    .getJSONArray("records")
                    .getJSONObject(i)
                    .getString("fonte_recurso_nome"));

            expense.setValor(Double.parseDouble(json.getJSONObject("result")
                    .getJSONArray("records")
                    .getJSONObject(i)
                    .getString("valor_pago").replace(",", ".")));

            expenses.add(expense);
        }

        return expenses;

    }

    //aqui eu inicio os metodos que vai agrupar os dados, atraves da classe Map
    public Map<Integer, Double> listarPorMes() {

        Map<Integer, Double> sum = new HashMap();

        for (Expense expense : expenses) {
            Double a;
            if (sum.containsKey(expense.getMes())) {
                a = sum.get(expense.getMes());
            } else {
                a = 0.0;
            }

            sum.put(expense.getMes(), expense.getValor() + a);

        }

        //Map<Integer, Double> sum = expenses.stream().collect(
        //Collectors.groupingBy(Expense::getMes, Collectors.summingDouble(Expense::getValor)));
        return sum;
    }

    public Map<String, Double> listarPorCategoria() {
        Map<String, Double> sum = new HashMap();

        for (Expense expense : expenses) {
            Double a;
            if (sum.containsKey(expense.getCategory())) {
                a = sum.get(expense.getCategory());
            } else {
                a = 0.0;
            }

            sum.put(expense.getCategory(), expense.getValor() + a);

        }

        //Map<String, Double> sum = expenses.stream().collect(
        //Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getValor)));
        return sum;
    }

    public Map<String, Double> listarPorOrigem() {

        Map<String, Double> sum = new HashMap();

        for (Expense expense : expenses) {
            Double a;
            if (sum.containsKey(expense.getSource())) {
                a = sum.get(expense.getSource());
            } else {
                a = 0.0;
            }

            sum.put(expense.getSource(), expense.getValor() + a);

        }
        //Map<String, Double> sum = expenses.stream().collect(
        //Collectors.groupingBy(Expense::getSource, Collectors.summingDouble(Expense::getValor)));

        return sum;
    }

}
