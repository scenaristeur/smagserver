package favedave.smag.objets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

public class ObjetConnecte {
	JSONObject jsonParameters = new JSONObject();
	private String nomObjet;
	private String adresseIpObjet;
	private String portObjet;
	private String commentaireObjet;
	private String emailNouvelObjet;
	private String date;
	private String id;

	public String setJsonParamaters(JSONObject data) {
		JSONObject receivedTemp = new JSONObject(data);
		Map<String, String> out = new HashMap<String, String>();
		parse(receivedTemp, out);
		nomObjet = data.get("nomObjet").toString();
		adresseIpObjet = data.get("adresseIpObjet").toString();
		portObjet = data.get("portObjet").toString();
		commentaireObjet = data.get("commentaireObjet").toString();
		emailNouvelObjet = data.get("emailNouvelObjet").toString();
		date = data.get("date").toString();
		id = "O" + date;
		System.out.println("creation de l'objet " + nomObjet
				+ " avec l'adresse IP" + adresseIpObjet + " et ID : " + id);
		String resultatInsertion = insertionObjetConnecte();

		return resultatInsertion;
	}

	private String insertionObjetConnecte() {
		UpdateRequest ur = new UpdateRequest();
		UpdateProcessor up;
		String service = "http://fuseki-smag0.rhcloud.com/ds/update";
		String update;
		update = "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		update += "PREFIX smag:   <http://smag0.blogspot.fr/ns/smag0#>";
		update += "PREFIX dc: <http://purl.org/dc/elements/1.1/>";

		update += "INSERT DATA {";
		update += "GRAPH <http://smag0.blogspot.fr/GraphTest>{";

		update += "smag:" + id + "    rdf:type         smag:ObjetConnecte .";
		update += "smag:" + id + "   dc:title         \"" + nomObjet + "\" .";
		update += "smag:" + id + "   smag:adresseIpObjet         \""
				+ adresseIpObjet + "\" .";
		update += "smag:" + id + "   smag:portObjet       \"" + portObjet
				+ "\" .";
		update += "smag:" + id + "   dc:description         \""
				+ commentaireObjet + "\" .";
		update += "smag:" + id + "   dc:emailGestionnaire        \""
				+ emailNouvelObjet + "\" .";
		/*
		 * update += "ex:cat     rdfs:subClassOf  ex:animal ."; update +=
		 * "zoo:host   rdfs:range       ex:animal ."; update +=
		 * "ex:zoo1    zoo:host         ex:cat2 ."; update +=
		 * "ex:cat3    owl:sameAs       ex:cat2 .";
		 */
		update += "}}";

		System.out.println(update);
		// ur.add("INSERT {<bouuula> <bouuuulb> <bouuulc>} WHERE {?s ?p ?o}");
		// ur.add("INSERT DATA { <http://website.com/exmp/something> http://purl.org/dc/elements/1.1/title ‘Some title’}");
		ur.add(update);

		up = UpdateExecutionFactory.createRemote(ur, service);
		up.execute();
		System.out.println("objet inséré");
		return ("ok");

	}

	public static Map<String, String> parse(JSONObject json,
			Map<String, String> out) throws JSONException {
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			String val = null;
			try {
				JSONObject value = json.getJSONObject(key);
				parse(value, out);
			} catch (Exception e) {
				val = json.getString(key);
			}

			if (val != null) {
				out.put(key, val);
			}
		}
		return out;
	}

}
