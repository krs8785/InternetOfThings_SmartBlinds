package edu.rit.csci759.fuzzylogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.RuleExpression;
import net.sourceforge.jFuzzyLogic.rule.RuleTerm;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodAndMin;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodOrMax;

public class MyBlindClass {
	
	static FunctionBlock main_fb = new FunctionBlock(null);
	public MyBlindClass() {
		// TODO Auto-generated constructor stub
		initializeFunctionBlockFromFile();
	}
	
	public void initializeFunctionBlockFromFile() {
		// TODO Auto-generated constructor stub
		String filename = "FuzzyLogic/sensor.fcl";
		FIS fis = FIS.load(filename, true);

		if (fis == null) {
			System.err.println("Can't load file: '" + filename + "'");
			System.exit(1);
		}

		// Get default function block
		main_fb = fis.getFunctionBlock(null);
		
	}

	public synchronized String calculateFuzzy(int temp, int amb) {
//		String filename = "FuzzyLogic/sensor.fcl";
//		FIS fis = FIS.load(filename, true);
//
//		if (fis == null) {
//			System.err.println("Can't load file: '" + filename + "'");
//			System.exit(1);
//		}
//
//		// Get default function block
//		FunctionBlock fb = fis.getFunctionBlock(null);
		double max=0;
		String whichToBlink = "";
		FunctionBlock fb = main_fb;
		// Set inputs
		fb.setVariable("ambient", amb);
		fb.setVariable("temperature", temp);

		// Evaluate
		fb.evaluate();

		// Show output variable's chart
		fb.getVariable("blind").defuzzify();
		
		HashMap<String, RuleBlock> map = fb.getRuleBlocks();

		Iterator itr = map.keySet().iterator();
		RuleBlock ruleBlock;

		HashMap<String, List<String>> rulesMap = new HashMap<String, List<String>>();

		while (itr.hasNext()) {
			// System.out.println("value1111:"+itr.next());
			ruleBlock = (RuleBlock) map.get(itr.next());
			List<Rule> ruleList = ruleBlock.getRules();

			for (Rule r : ruleList) {
				
				/* System.out.println("rule::" + r);
				  System.out.println("r.getName():" + r.getName());
				  System.out.println("r.getName()11:" + r.getWeight());*/
				 // System.out.println("r.getName()12:" +
				 // r.getDegreeOfSupport()); 
//				  System.out.println("r.getName()13:"
//				  + r.getAntecedents()); System.out.println("r.getName()14:" +
//				  r.getAntecedents().getTerm1());
//				  System.out.println("r.getName()15:" +
//				  r.getAntecedents().getTerm2());
//				  System.out.println("r.getName()16:" +
//				  r.getAntecedents().getRuleConnectionMethod());
//				  System.out.println("r.getName()17:" + r.getConsequents());
				
				if(max <= r.getDegreeOfSupport()){
					max = r.getDegreeOfSupport();
					String tempo = r.getConsequents().get(0).toString();
					String[] space = tempo.split(" ");
					whichToBlink = space[2];
					
				}
				 
			}
		}
		//System.out.println("selcetd max is "+max );
		System.out.println("Whch to blink "+whichToBlink);
		// Print ruleSet
		//return fb.getVariable("blind").getValue();
		return whichToBlink;
	}

	public HashMap<String, List<String>> getRules() {
		System.out.println("Entering getrules");
//		String filename = "FuzzyLogic/sensor.fcl";
//		FIS fis = FIS.load(filename, true);
//
//		if (fis == null) {
//			System.err.println("Can't load file: '" + filename + "'");
//			System.exit(1);
//		}
//
//		// Get default function block
//		FunctionBlock fb = fis.getFunctionBlock(null);
		FunctionBlock fb = main_fb;

		HashMap<String, RuleBlock> map = fb.getRuleBlocks();

		Iterator itr = map.keySet().iterator();
		RuleBlock ruleBlock;

		HashMap<String, List<String>> rulesMap = new HashMap<String, List<String>>();

		while (itr.hasNext()) {
			// System.out.println("value1111:"+itr.next());
			ruleBlock = (RuleBlock) map.get(itr.next());
			List<Rule> ruleList = ruleBlock.getRules();

			for (Rule r : ruleList) {
				/*
				 * System.out.println("rule::" + r);
				 * System.out.println("r.getName():" + r.getName());
				 * System.out.println("r.getName()11:" + r.getWeight());
				 * System.out.println("r.getName()12:" +
				 * r.getDegreeOfSupport()); System.out.println("r.getName()13:"
				 * + r.getAntecedents()); System.out.println("r.getName()14:" +
				 * r.getAntecedents().getTerm1());
				 * System.out.println("r.getName()15:" +
				 * r.getAntecedents().getTerm2());
				 * System.out.println("r.getName()16:" +
				 * r.getAntecedents().getRuleConnectionMethod());
				 * System.out.println("r.getName()17:" + r.getConsequents());
				 */

				String[] tempVal = null;
				List<String> ruleValues = new ArrayList<String>();

				if (null != r.getAntecedents().getTerm2()) {
					// first antecedent
					tempVal = (r.getAntecedents().getTerm1().toString())
							.split("IS");
					ruleValues.add(0, tempVal[1].trim());
					// second antecedent
					tempVal = (r.getAntecedents().getTerm2().toString())
							.split("IS");
					ruleValues.add(1, tempVal[1].trim());
					// relation
					tempVal = (r.getAntecedents().getRuleConnectionMethod()
							.toString()).split(":");
					ruleValues.add(2, tempVal[0].trim());
				} else {
					// first antecedent
					tempVal = (r.getAntecedents().getTerm1().toString())
							.split("IS");

					if (tempVal[0].equals("temperature")) {
						ruleValues.add(0, tempVal[1].trim());
						ruleValues.add(1, "NA");
						ruleValues.add(2, "NA");
					} else {
						ruleValues.add(0, "NA");
						ruleValues.add(1, tempVal[1].trim());
						ruleValues.add(2, "NA");
					}

				}
				// consequent
				tempVal = (r.getConsequents().get(0).toString()).split("IS");
				ruleValues.add(3, tempVal[1].trim());

				rulesMap.put(r.getName(), ruleValues);
			}
		}
		return rulesMap;
	}

	@SuppressWarnings("deprecation")
	public void setRules(HashMap<String, List<String>> rulesList) {

		System.out.println("Entering the setRules");
		
		
		//printing the new rules
		
		/*for(Map.Entry<String, List<String>> xyz : rulesList.entrySet()){
			List<String> temp = xyz.getValue();
			System.out.println("Rule "+xyz.getKey()+"IF Temperature IS "+ temp.get(0)+" "+temp.get(2)+" IF Ambient IS "+temp.get(1)
   	 				+" Then Blind Is "+temp.get(3));
		}*/
		
		//sorting the new rules (Sorting the hashmap)
		System.out.println("sorting new rules...wait...");
		Map<String,List<String>> sortedRules  = new TreeMap<String, List<String>>(rulesList);
		System.out.println("after sorting");
		/*for(Map.Entry<String, List<String>> xyz : sortedRules.entrySet()){
			List<String> temp = xyz.getValue();
			System.out.println("Rule "+xyz.getKey()+"IF Temperature IS "+ temp.get(0)+" "+temp.get(2)+" IF Ambient IS "+temp.get(1)
   	 				+" Then Blind Is "+temp.get(3));
		}*/
		System.out.println("done sorting ");
		
//		String filename = "FuzzyLogic/sensor.fcl";
//		FIS fis = FIS.load(filename, true);
//		if (fis == null) {
//			System.err.println("Can't load file: '" + filename + "'");
//			System.exit(1);
//		}
//
//		// Get default function block
//		FunctionBlock fb = fis.getFunctionBlock(null);
		
		FunctionBlock fb = main_fb;
		
		HashMap<String, RuleBlock> map = fb.getRuleBlocks();
		//System.out.println(map);

		
		RuleBlock ruleBlock;		
		ruleBlock = map.get("No1");
		
		System.out.println(ruleBlock);
		
		System.out.println("------------------");
		
		int count = ruleBlock.getRules().size();
		
		for(int i=0;i<count;i++){
			ruleBlock.remove(ruleBlock.getRules().get(0));
		}
		 
		
		for(Map.Entry<String, List<String>> xyz : sortedRules.entrySet()){
			//new list of rules 
			List<String> item = xyz.getValue();
			
			
			Rule newRule= new Rule(xyz.getKey(), ruleBlock);
			RuleExpression anteAnd = new RuleExpression();
			if(item.get(0).equals("NA")){
				//If only ambient
				RuleTerm term2 = new RuleTerm(fb.getVariable("ambient"),
						item.get(1).toLowerCase(), false);
				anteAnd.setTerm2(term2);
				//RuleTerm term1 = null;
				//anteAnd = new RuleExpression(term1, term2,
						//null);
				
 			}else if (item.get(1).equals("NA")){
				//if only temperature
				RuleTerm term1 = new RuleTerm(fb.getVariable("temperature"),
						item.get(0).toLowerCase(), false);
				anteAnd.setTerm1(term1);
				/*RuleTerm term2 = null;
				anteAnd = new RuleExpression(term1, term2,
						null);*/
			}else{
				//both
				RuleTerm term1 = new RuleTerm(fb.getVariable("temperature"),
						item.get(0).toLowerCase(), false);
				RuleTerm term2 = new RuleTerm(fb.getVariable("ambient"),
					item.get(1).toLowerCase(), false);			
				
				//AND or OR
				
				if("AND".equals(item.get(2))){
					anteAnd = new RuleExpression(term1, term2,
							RuleConnectionMethodAndMin.get());
				}else {
					anteAnd = new RuleExpression(term1, term2,
							RuleConnectionMethodOrMax.get());	
				}
				
			}	
			newRule.setAntecedents(anteAnd);
			
			newRule.addConsequent(fb.getVariable("blind"), item.get(3).toLowerCase(),
					false);
			ruleBlock.add(newRule);
			//System.out.println(ruleBlock);
		}
		
		
		//ruleBlock.setRules(temp);
		System.out.println("after new all rules");
		System.out.println(ruleBlock);
		System.out.println("blah.........");
		
		
		//System.out.println(map);
		
		String filename1 = "FuzzyLogic/sensor.fcl";
		//to write to fcl file
				
				File file;
				BufferedWriter br = null;
				try {

					file = new File(filename1);
					br = new BufferedWriter(new FileWriter(file));
					br.write(fb.toStringFcl());

				} catch (IOException e) {

				} finally {
					try {
						
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
		//System.out.println("current fb:"+fb);
		//System.out.println("Initialising the fb thingi");
				initializeFunctionBlockFromFile();
	}

	public static void main(String[] args) throws Exception {
		MyBlindClass test = new MyBlindClass();
		//double output = test.calculateFuzzy(80, 82);
		// getRules();
		// System.out.println("blind value :: " + output);

		/*
		 * String filename = "FuzzyLogic/sensor.fcl"; FIS fis =
		 * FIS.load(filename, true);
		 * 
		 * if (fis == null) { System.err.println("Can't load file: '" + filename
		 * + "'"); System.exit(1); }
		 * 
		 * // Get default function block FunctionBlock fb =
		 * fis.getFunctionBlock(null);
		 * 
		 * // Set inputs fb.setVariable("ambient", amb);
		 * fb.setVariable("temperature", temp);
		 * 
		 * // Evaluate fb.evaluate();
		 * 
		 * // Show output variable's chart fb.getVariable("tip").defuzzify();
		 * 
		 * // Print ruleSet System.out.println(fb); System.out.println("Tip: " +
		 * fb.getVariable("tip").getValue());
		 */
	}
}
