import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Information {


    API api = new API();
    GetRequestRepository repository = new GetRequestRepository(api);

    public JsonObject Information(){


        //Fetching the starship "Death Star" and relevant values
        JsonObject jsonObject = repository.getAll("starships", "Death");
        JsonArray results = jsonObject.getAsJsonArray("results");
        JsonObject starship = results.get(0).getAsJsonObject();

        //Ading values of name, class and model in a Json object.
        JsonObject apiResult = new JsonObject();
        apiResult.add("name", starship.get("name"));
        apiResult.add( "class", starship.get("starship_class"));
        apiResult.add( "model", starship.get("model"));

        //Creating a Json result which would be retuned as response as the "starship".
        JsonObject jsonResult = new JsonObject();
        jsonResult.add("starship", apiResult);


        // Get Values of the Starship "Darth" is using.
        jsonObject = repository.getAll("people", "Darth");
        results = jsonObject.getAsJsonArray("results");
        starship = results.get(0).getAsJsonObject();


        // Getting URI of the starship
        JsonArray startshipURL = starship.getAsJsonArray("starships");

        //Removing double quotes from the resultset
        StringBuilder sb1 = new StringBuilder(startshipURL.get(0).toString());
        sb1.deleteCharAt(startshipURL.get(0).toString().length() - 1);
        sb1.deleteCharAt(0);

        //Getting the crew value from the json response.
        jsonObject = repository.innerRequest(sb1.toString());
        String res = jsonObject.get("crew").toString();
        if(res != null)
            jsonResult.addProperty(
                    "crew", res);
        else
            jsonResult.addProperty("crew", 0);


        // Fetching detailsofteh planet inwhich Princess "Leia" is residing.
        jsonObject = repository.getAll("people", "Leia");
        results = jsonObject.getAsJsonArray("results");
        JsonObject planet = results.get(0).getAsJsonObject();


        String homeworldURL = planet.get("homeworld").toString();
        StringBuilder sb = new StringBuilder(homeworldURL);
        sb.deleteCharAt(homeworldURL.length() - 1);
        sb.deleteCharAt(0);


        jsonObject = repository.innerRequest(sb.toString());
        String results2 = jsonObject.getAsJsonPrimitive("name").toString();
        StringBuilder sb2 = new StringBuilder(results2);
        sb2.deleteCharAt(results2.length() - 1);
        sb2.deleteCharAt(0);

        //Validating of the planet in which Leia is "Alderaan".
        // If planet is Alderaan true is returned for isLeiaOnPlanet
        // If the panet is not Alderaan false is returned for isLeiaOnPlanet.
        if(sb2.toString().equals("Alderaan"))
            jsonResult.addProperty("isLeiaOnPlanet", true);
        else
            jsonResult.addProperty("isLeiaOnPlanet", false);

        // Retunrning resultset as a json object.
        return jsonResult;


    }
}
