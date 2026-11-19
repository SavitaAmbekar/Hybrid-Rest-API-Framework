package api.utilities;

import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataProviders {

    // Fixed missing '//' after user.dir and matched 'Userdata.xlsx' filename
    String path = System.getProperty("user.dir") + "//testData//Userdata.xlsx";
    XLUtility xlUtility = new XLUtility(path);

    @DataProvider(name = "Data")
    public Object[][] getAllData() throws IOException {
        int rownum = xlUtility.getRowCount("Sheet1");
        int colnum = xlUtility.getCellCount("Sheet1", 1);

        Object[][] apiData = new Object[rownum][1];

        for (int i = 1; i <= rownum; i++) {
            Map<String, String> rowMap = new HashMap<>();
            for (int j = 0; j < colnum; j++) {
                String key = xlUtility.getCellData("Sheet1", 0, j);
                String value = xlUtility.getCellData("Sheet1", i, j);
                rowMap.put(key, value);
            }
            apiData[i - 1][0] = rowMap;
        }

        return apiData;
    }

    @DataProvider(name = "UserNames")
    public String[] getUserNames() throws IOException {
        int rownum = xlUtility.getRowCount("Sheet1");
        String[] apiData = new String[rownum];

        for (int i = 1; i <= rownum; i++) {
            apiData[i - 1] = xlUtility.getCellData("Sheet1", i, 1);
        }

        return apiData;
    }
}
