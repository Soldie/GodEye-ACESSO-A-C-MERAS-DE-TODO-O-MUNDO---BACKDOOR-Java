package dev.adrielgro;

import com.fooock.shodan.ShodanRestApi;
import com.fooock.shodan.model.banner.Banner;
import com.fooock.shodan.model.host.HostReport;
import io.reactivex.observers.DisposableObserver;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Surveillance {
    ShodanRestApi api = new ShodanRestApi("WdeGN3PRx1eXASI3uGtf4yLj1irODZBl");
    List<List<String>> dataList = new ArrayList<List<String>>();

    public String[][] shodanSearch() {
        api.hostSearch("rtsp dahua")
                .subscribe(new DisposableObserver<HostReport>() {
                    @Override
                    public void onComplete() {
                        JOptionPane.showMessageDialog(null, "Busqueda completada!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        JOptionPane.showMessageDialog(null, "Ocurrió un error: " + e.toString());
                    }

                    @Override
                    public void onNext(HostReport hostReport) {
                        try {
                            // Shodan
                            int total = hostReport.getTotal();
                            List<Banner> banners = hostReport.getBanners();


                            // Archivo
                            //new BufferedWriter(new FileWriter("data.obj")); // Crear archivo
                            FileOutputStream fileOut = new FileOutputStream("data.obj"); // Cargar archivo
                            ObjectOutputStream dataFile = new ObjectOutputStream(fileOut);

                            for(Banner banner : banners) {
                                List<String> dataRow = new ArrayList<String>(); // Creamos un arreglo temporal
                                // List dataRow = new ArrayList();

                                dataRow.add(banner.getLocation().getCountryCode()); // Localizacion
                                dataRow.add(banner.getIpStr()); // IP Address
                                dataRow.add((String.valueOf(banner.getPort()))); // Port

                                dataList.add(dataRow);
                            }
                            dataFile.writeObject(dataList); // Escribimos la busqueda en el archivo
                            dataFile.close(); // Cerramos el archivo
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });



        List obj1 = null;
        try {
            FileInputStream fileIn = new FileInputStream("data.obj"); // Cargar archivo
            ObjectInputStream entrada = new ObjectInputStream(fileIn);

            obj1 = (List)entrada.readObject();

            System.out.println(obj1.toString());
            entrada.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Convertir lista a arreglo bidimensional
        String[][] array = new String[dataList.size()][];
        String[] blankArray = new String[0];
        for(int i=0; i < dataList.size(); i++) {
            array[i] = dataList.get(i).toArray(blankArray);
        }

        //Object[] testArray = obj1.toArray();

        //return testArray;
        return array;
    }


}
