package edu.asu.msse.semanticweb.group6.knowyourcity.sparql;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

import edu.asu.msse.semanticweb.group6.knowyourcity.model.City;
import edu.asu.msse.semanticweb.group6.knowyourcity.model.Condo;
import edu.asu.msse.semanticweb.group6.knowyourcity.model.OneBedroom;
import edu.asu.msse.semanticweb.group6.knowyourcity.model.TwoBedroom;
import edu.asu.msse.semanticweb.group6.knowyourcity.model.Walmart;
import edu.asu.msse.semanticweb.group6.knowyourcity.model.Zipcode;

public class SparqlQueryEngine {

	static String defaultNameSpace = "http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#";
	private static SparqlQueryEngine instance = null;

	private static Model model = null;
	private static Model schema = null;

	public SparqlQueryEngine() {
		super();
		if (model == null) {
			model = ModelFactory.createOntologyModel();
			populateOWLSchema();
			populateStateCityZip();
			populateHomeFair();
			populateOneBed();
			populateTwoBed();
			populateHousingCondo();
			populatWalmarts();
		}
	}

	private void populateOWLSchema() {
		try {
			InputStream inSchema = getClass().getResourceAsStream("/ontologies/know_your_city.owl");
			schema = ModelFactory.createOntologyModel();
			// Use local copy for demos without network connection
			schema.read(inSchema, defaultNameSpace);
			inSchema.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void populateStateCityZip() {
		InputStream stateCityZip = getClass().getResourceAsStream("/ontologies/state-city-zip.rdf");
		model.read(stateCityZip, defaultNameSpace);
		try {
			stateCityZip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void populateOneBed() {
		InputStream oneBedStream = getClass().getResourceAsStream("/ontologies/housing_1bed.rdf");
		model.read(oneBedStream, defaultNameSpace);
		try {
			oneBedStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void populateTwoBed() {
		InputStream twoBedStream = getClass().getResourceAsStream("/ontologies/housing_2bed.rdf");
		model.read(twoBedStream, defaultNameSpace);
		try {
			twoBedStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void populateHousingCondo() {
		InputStream housingCondo = getClass().getResourceAsStream("/ontologies/housing_condo.rdf");
		model.read(housingCondo, defaultNameSpace);
		try {
			housingCondo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void populateHomeFair() {
		InputStream homefairStream = getClass().getResourceAsStream("/ontologies/homefair.rdf");
		model.read(homefairStream, defaultNameSpace);
		try {
			homefairStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void populatWalmarts() {
		// model = ModelFactory.createOntologyModel();
		InputStream walmartStream = getClass().getResourceAsStream("/ontologies/walmart_loc.rdf");
		model.read(walmartStream, defaultNameSpace);
		try {
			walmartStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private StringBuffer prepareForQuery() {
		StringBuffer queryStr = new StringBuffer();
		City city = new City();
		// Establish Prefixes
		// Set default Name space first
		queryStr.append("PREFIX xsd" + ": <" + "http://www.w3.org/2001/XMLSchema#" + "> ");
		queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdf-schema#" + "> ");
		queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-" + "rdf-syntax-ns#" + "> ");
		queryStr.append("PREFIX info" + ": <" + "http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city"
				+ "> ");

		return queryStr;
	}

	public List<Map> runGetCitiesForStateQuery(String statename) {
		List<Map> list = new ArrayList<Map>();
		StringBuffer queryStr = prepareForQuery();
		queryStr.append(
				"select ?name ?sub where{?sub <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#belongs_to_state> <http://127.0.0.1:3333/state/"
						+ statename.toLowerCase()
						+ ">. ?sub <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_name> ?name } ");
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?name");
				RDFNode uri = soln.get("?sub");
				if (name != null) {
					Map map = new HashMap();
					map.put("uri", uri.toString());
					map.put("name", name.toString());
					list.add(map);
				}
			}
		} finally {
			qexec.close();
		}
		list.sort(new Comparator<Map>() {

			@Override
			public int compare(Map o1, Map o2) {
				String name1 = (String) o1.get("name");
				String name2 = (String) o2.get("name");
				return name1.compareTo(name2);
			}

		});
		return list;
	}

	public City runZipcodeInfoForCityUri(String cityuri) {
		Set<String> set = new HashSet<String>();
		StringBuffer queryStr = prepareForQuery();
		City city = new City();
		List<Zipcode> zipcodes = new ArrayList<Zipcode>();
		// Now add query
		queryStr.append("select ?zipcodename where {<" + cityuri
				+ "> <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_zipcodes> ?zipcode.?zipcode <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#is_a_zipcode> ?zipcodename.  }");
		// System.out.println(queryStr);
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?zipcodename");
				if (name != null) {
					String[] splitname = name.toString().split("\\^");
					set.add(splitname[0]);
				}
			}
			for (String zip : set) {
				Zipcode zipcode = runZipcodeInfo(zip);
				zipcode.setOneBedroom(getOneBedroomInfo(zip));
				zipcode.setTwoBedroom(getTwoBedroomInfo(zip));
				zipcode.setCondo(getCondoInfo(zip));
				zipcode.setWalmarts(findStores(zip));
				zipcodes.add(zipcode);
			}
			zipcodes.sort(new Comparator<Zipcode>() {
				@Override
				public int compare(Zipcode o1, Zipcode o2) {
					return o1.getZipcode()-o2.getZipcode();
				}
			});
			city.setZipcodes(zipcodes);
		} finally {
			qexec.close();
		}
		return city;

	}

	public Set<String> runGetStatesQuery() {
		Set<String> set = new TreeSet<String>();
		StringBuffer queryStr = prepareForQuery();

		queryStr.append(
				"select ?name where{?sub rdf:type <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#State>. ?sub <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_name> ?name } ");
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?name");
				if (name != null && !name.toString().equals("")) {
					set.add(name.toString());
				}
			}
		} finally {
			qexec.close();
		}
		return set;
	}

	public Zipcode runZipcodeInfo(String zip) {
		Zipcode zipcodeinfo = new Zipcode();
		StringBuffer queryStr = prepareForQuery();
		zipcodeinfo.setZipcode(Integer.parseInt(zip));
		// Now add query
		queryStr.append(
				" select  ?median_time_to_work ?earthquake_risk ?air_pollution_index ?crime_risk where{<http://127.0.0.1:3333/zip_code/"
						+ zip
						+ "> <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_mean_transport_time> ?median_time_to_work."
						+ "<http://127.0.0.1:3333/zip_code/" + zip
						+ "> <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_air_pollution_index> ?air_pollution_index."
						+ "<http://127.0.0.1:3333/zip_code/" + zip
						+ "> <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_crime_risk> ?crime_risk."
						+ "<http://127.0.0.1:3333/zip_code/" + zip
						+ "> <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_earthquake_risk> ?earthquake_risk.} ");
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode mediantimetowork = soln.get("?median_time_to_work");
				RDFNode crimerisk = soln.get("?crime_risk");
				RDFNode earthquakerisk = soln.get("?earthquake_risk");
				RDFNode airpollutionindex = soln.get("?air_pollution_index");

				if (mediantimetowork != null) {
					String[] test = mediantimetowork.toString().split("\\^");
					zipcodeinfo.setMedianTime(Double.parseDouble(test[0]));
				} else
					zipcodeinfo.setMedianTime(-1);
				if (crimerisk != null) {
					String[] test = crimerisk.toString().split("\\^");
					zipcodeinfo.setCrimeRisk((Double.parseDouble(test[0])));
				} else
					zipcodeinfo.setCrimeRisk(-1);
				if (earthquakerisk != null) {
					String[] test = earthquakerisk.toString().split("\\^");
					zipcodeinfo.setEarthquakeRisk((Double.parseDouble(test[0])));
				} else
					zipcodeinfo.setEarthquakeRisk(-1);
				if (airpollutionindex != null) {
					String[] test = airpollutionindex.toString().split("\\^");
					zipcodeinfo.setAirPollutionIndex((Double.parseDouble(test[0])));
				} else
					zipcodeinfo.setAirPollutionIndex(-1);
			}
		} finally {
			qexec.close();
		}
		return zipcodeinfo;
	}

	public OneBedroom getOneBedroomInfo(String zip) {
		OneBedroom onebedroom = new OneBedroom();
		StringBuffer queryStr3 = prepareForQuery();

		String queryonebed = "select ?rent where{?onebed rdf:type <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#OneBedroom>. ?onebed <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#is_associated_with_zipcode>"
				+ " <http://127.0.0.1:3333/zip_code/" + zip
				+ ">. ?onebed <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_median_rent> ?rent. }";

		queryStr3.append(queryonebed);

		Query query3 = QueryFactory.create(queryStr3.toString());
		QueryExecution qexec3 = QueryExecutionFactory.create(query3, model);

		try {
			ResultSet response3 = qexec3.execSelect();

			while (response3.hasNext()) {
				QuerySolution soln3 = response3.nextSolution();
				RDFNode rent_onebed = soln3.get("?rent");
				if (rent_onebed != null) {
					Double medianrent_onebed = Double.parseDouble(rent_onebed.toString());
					onebedroom.setMedianRent(medianrent_onebed);
				} else
					return null;
			}
		} catch (Exception ex) {

		} finally {
			qexec3.close();
		}
		return onebedroom;
	}

	public TwoBedroom getTwoBedroomInfo(String zip) {
		TwoBedroom twobedroom = new TwoBedroom();
		StringBuffer queryStr4 = prepareForQuery();
		String querytwobed = "select ?rent where{?twobed rdf:type <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#TwoBedroom>. ?twobed <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#is_associated_with_zipcode>"
				+ " <http://127.0.0.1:3333/zip_code/" + zip
				+ ">. ?twobed <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_median_rent> ?rent. }";

		queryStr4.append(querytwobed);

		Query query4 = QueryFactory.create(queryStr4.toString());
		QueryExecution qexec4 = QueryExecutionFactory.create(query4, model);

		try {
			ResultSet response4 = qexec4.execSelect();

			while (response4.hasNext()) {
				QuerySolution soln4 = response4.nextSolution();
				RDFNode rent_twobed = soln4.get("?rent");

				if (rent_twobed != null) {

					Double medianrent_twobed = Double.parseDouble(rent_twobed.toString());
					twobedroom.setMedianRent(medianrent_twobed);
				}

				else
					return null;

			}
		} catch (Exception ex) {

		} finally {
			qexec4.close();
		}
		return twobedroom;
	}

	public Condo getCondoInfo(String zip) {
		Condo condostyle = new Condo();
		StringBuffer queryStr5 = prepareForQuery();

		String querycondo = "select ?rent where{?condo rdf:type <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#Condo>. ?condo <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#is_associated_with_zipcode>"
				+ " <http://127.0.0.1:3333/zip_code/" + zip
				+ ">. ?condo <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_median_rent> ?rent. }";

		queryStr5.append(querycondo);

		Query query5 = QueryFactory.create(queryStr5.toString());
		QueryExecution qexec5 = QueryExecutionFactory.create(query5, model);

		try {
			ResultSet response5 = qexec5.execSelect();

			while (response5.hasNext()) {
				QuerySolution soln5 = response5.nextSolution();
				RDFNode rent_condo = soln5.get("?rent");

				if (rent_condo != null) {

					Double medianrent_condo = Double.parseDouble(rent_condo.toString());
					condostyle.setMedianRent(medianrent_condo);
				} else
					return null;
			}
		} catch (Exception ex) {

		} finally {
			qexec5.close();
		}
		return condostyle;
	}

	public List<Walmart> findStores(String zip) {
		List<Walmart> walmarts = new ArrayList<Walmart>();
		StringBuffer queryStr2 = prepareForQuery();

		String querystores = "select ?addr where{?store rdf:type <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#Walmart>. ?store <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#is_at_zipcode>"
				+ " <http://127.0.0.1:3333/zip_code/" + zip
				+ ">. ?store <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_address> ?addr. }";
		queryStr2.append(querystores);

		Query query2 = QueryFactory.create(queryStr2.toString());
		QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);

		try {
			ResultSet response2 = qexec2.execSelect();

			while (response2.hasNext()) {
				QuerySolution soln2 = response2.nextSolution();
				RDFNode storeAddr = soln2.get("?addr");
				if (storeAddr != null) {
					Walmart walmart = new Walmart();
					String walmart_loc = storeAddr.toString();
					walmart.setAddress(walmart_loc);
					walmarts.add(walmart);
				}
			}
		} catch (Exception ex) {

		} finally {
			qexec2.close();
		}

		return walmarts;
	}

	public String runGetCityNameQuery(String cityuri) {
		String cityname = "";
		StringBuffer queryStr = prepareForQuery();
		queryStr.append("select ?name where{<" + cityuri
				+ "> <http://www.semanticweb.org/japas_000/ontologies/2015/10/know-your-city#has_name> ?name}");
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet response = qexec.execSelect();
			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?name");
				if (name != null)
					cityname = name.toString();
				else
					cityname = "not found";
			}
		} finally {
			qexec.close();
		}
		return cityname;
	}

	public static void main(String[] args) {
		SparqlQueryEngine engine = new SparqlQueryEngine();
		Zipcode test = engine.runZipcodeInfo("85281");
		City city = engine.runZipcodeInfoForCityUri("http://127.0.0.1:3333/maricopa/phoenix");
		System.out.println(test.getZipcode());
		System.out.println(test.getAirPollutionIndex());
		System.out.println(test.getCrimeRisk());
		System.out.println(test.getMedianTime());
		System.out.println(test.getEarthquakeRisk());
	}

	public static SparqlQueryEngine getInstance() {
		if (instance == null)
			instance = new SparqlQueryEngine();
		return instance;
	}
}
