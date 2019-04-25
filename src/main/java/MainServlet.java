import databases.MySqlClass;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MainServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MainServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuilder jb = new StringBuilder();
        String line = null;

        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            LOGGER.warning(e.toString());
        }

        try {
            JSONObject jsonObject = new JSONObject(jb.toString());

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            int command = jsonObject.getInt("command");

            switch (command) {

                case 0:

                    ArrayList<String> names = MySqlClass.getAllNames();

                    JSONObject jsonToReturn0 = new JSONObject();
                    jsonToReturn0.put("answer", "names");
                    jsonToReturn0.put("list", names.toString());
                    out.println(jsonToReturn0.toString());

                    break;

                case 1:

                    String data = jsonObject.getString("name");

                    MySqlClass.addName(data);

                    JSONObject jsonToReturn1 = new JSONObject();
                    jsonToReturn1.put("answer", "ok");
                    out.println(jsonToReturn1.toString());

                    break;

                default:
                    System.out.println("default switch");
                    break;

            }
        } catch (Exception e) {
            LOGGER.warning(e.toString());
        }
    }
}