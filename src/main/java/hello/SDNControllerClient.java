package hello;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import jdk.nashorn.internal.ir.ObjectNode;
import com.google.common.collect.Lists;
import org.apache.tomcat.util.codec.binary.Base64;
//import org.springframework.boot.autoconfigure.data.mongo.ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;



import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.util.*;


//import persist.BlockingFlow;


//@Component



//@EnableJpaRepositories(basePackageClasses= {BlockingFlowRepository.class})
@SpringBootApplication
public class SDNControllerClient {



   @Autowired
    BlockingFlowRepository blockingFlowRepository;

   //

    @PostConstruct
    public void init() {
        System.out.println("SDN Controller Client start");
      //  this.ViewNodes();
       // this.ViewNodes("openflow:246321139094487");

       this.SyncRepository("openflow:246321139094487",0);

         this.DeleteFlow("openflow:246321139094487",0,"555");
        try {
            this.CreateFlow("openflow:246321139094487",0,"555");
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
 //           this.DeleteFlow("openflow:246321139094487",0,"555");
            System.out.println("FAIL:" + e.getMessage());
        }


    }

    public void ViewNodes(){
        System.out.println("Flow create");
        RestTemplate restTemplate = new RestTemplate();

        String plainCreds = "admin:admin";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");
        headers.add("Authorization","Basic " + base64Creds);

        HttpEntity request = new HttpEntity(headers);


        ResponseEntity<String> reply;
        String url;

        System.out.println("GET Nodes");
        url = "http://localhost:8181/restconf/operational/opendaylight-inventory:nodes";

        // ResponseEntity<String> reply = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        reply = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        System.out.println("Response: "+reply.getStatusCode());
        System.out.println("body:"+ reply.getBody()  );




        System.out.println("GET Nodes");
        //HttpEntity request = new HttpEntity(headers);
        url = "http://localhost:8181/restconf/operational/opendaylight-inventory:nodes";

        reply = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        System.out.println("Response: "+reply.getStatusCode());
        System.out.println("body:"+ reply.getBody()  );
    }

    public void ViewNodes(String args){
        System.out.println("Show Flow" + args);
        RestTemplate restTemplate = new RestTemplate();

        String plainCreds = "admin:admin";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");
        headers.add("Authorization","Basic " + base64Creds);

        HttpEntity request = new HttpEntity(headers);


        ResponseEntity<String> reply;
        String url;

        System.out.println("GET Nodes");
        url = "http://localhost:8181/restconf/operational/opendaylight-inventory:nodes/node/" + args;
        System.out.println(url);
        // ResponseEntity<String> reply = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        reply = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        System.out.println("Response: "+reply.getStatusCode());
        System.out.println("body:"+ reply.getBody()  );

    }

    public void SyncRepository(String nodeID, int tableID){
        //this get the flows from the SDNcontroller, map the flows into a Flow Object,
    //then extract the FLows that are Blockingflows and save those into the repository
        System.out.println("Sync BlockingFlows Repository");

        RestTemplate restTemplate = new RestTemplate();
        String plainCreds = "admin:admin";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization","Basic " + base64Creds);

        String url;
        url = "http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/" + nodeID + "/flow-node-inventory:table/" + tableID;
//        HttpEntity<String> entity = new HttpEntity<>(headers);

        HttpEntity<InventoryFlows> entity = new HttpEntity<InventoryFlows>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.GET,entity, String.class);

        //perse body to a flow
        ObjectMapper mapper = new ObjectMapper();
        //configure mapper
        //mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,true); //put root class name
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        try {
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode flowListNode = null;


            //iterate node
            for (JsonNode tableNode : rootNode.get("flow-node-inventory:table")) {
                if (tableNode.get("id").asInt() == tableID) {
                    flowListNode = tableNode.get("flow");
                    break;
                }}

                BlockingFlow[] bfparsedresult = new BlockingFlow[flowListNode.size()];
                for (int i = 0; i < bfparsedresult.length; i++){
                    JsonNode flowNode = flowListNode.get(i);
                    Flow flow = mapper.treeToValue(flowNode, Flow.class);
                    // TODO: verify that this is actually a blocking flow by looking at action
                    BlockingFlow blockingFlowParsed = new BlockingFlow(
                            flow.getId(),
                            Instant.now(),
                            flow.getTable_id(),
                            flow.getHardtimeout(),
                            flow.getPriority(),
                            flow.getMatch().getIpv4destination(),
                            flow.getFlowname()

                    );
                    bfparsedresult[i] = blockingFlowParsed;
                }

                for (hello.BlockingFlow blockingFlow2 : bfparsedresult){
                       Optional<BlockingFlow> old;
                    old = blockingFlowRepository.findById(blockingFlow2.getId());
                    if (!old.isPresent()) {
                        //SAVE FOUND FLOWS INTO REPO
                        blockingFlowRepository.save(new BlockingFlow(
                                blockingFlow2.getId(),
                                blockingFlow2.getCreationTime(),
                                blockingFlow2.getTable_id(),
                                blockingFlow2.getHardtimeout(),
                                blockingFlow2.getPriority(),
                                blockingFlow2.getIp_destination_match(),
                                blockingFlow2.getFlowname()
                        ));
                    } else {
                        //UPDATE EXISTINT
                        // old.setFlowName(blockingFlow.getFlowName());
                        //old.get().setHardtimeout((blockingFlow2.getHardtimeout()));
                        old.get().setHardtimeout(blockingFlow2.getHardtimeout());
                        old.get().setIp_destination_match(blockingFlow2.getIp_destination_match());
                        blockingFlowRepository.save(old.get());
                    }
                }




        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static ObjectMapper getObjectMapper() {
        //configure object mapper
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
                .modules(new JavaTimeModule())
                .build();
        return mapper;
    }

    public void CreateFlow(String nodeID, Integer tableID, String flowID) throws JsonProcessingException {
        System.out.println("Flow Create:");


        Match match = new Match();
        match.setIpv4destination("10.11.11.118/32");

        EtherMatch ethernetmatch = new EtherMatch(new EtherType(2048));
        match.setEthernetmatch(ethernetmatch);

        Action action = new Action();
        action.setOrder(1);
        action.setDropaction(new HashMap<>());

        ApplyActions applyactions = new ApplyActions();
        applyactions.setAction(Lists.newArrayList(action));

        Instruction instruction = new Instruction();
        instruction.setOrder(1);
        instruction.setApplyactions(applyactions);

        Instructions instructions = new Instructions();
        instructions.setInstruction(Lists.newArrayList(instruction));


        Flow flow = new Flow();
        flow.setId("555");
        flow.setInstallHw(false);
        flow.setTable_id(0);
        flow.setStrict(true);
        flow.setMatch(match);
        flow.setIdletimeout(0);
        flow.setFlowname("MuninnFlow1");
        flow.setBarrier(false);
        flow.setCookie_mask(555);
        flow.setCookie(4);
        flow.setPriority(65535);
//        flow.setInstructions(Lists.newArrayList(instruction));
        flow.setInstructions(instructions);

        InventoryFlows inventoryFLows = new InventoryFlows();
        inventoryFLows.setFlows(Lists.newArrayList(flow));



        //persist stuff

      BlockingFlow blockingflow = new BlockingFlow(flow.getId(),Instant.now(),flow.getTable_id(),flow.getHardtimeout(),flow.getPriority(),match.getIpv4destination(),flow.getFlowname());


      BlockingFLowDBService blockingflowdbservice = new BlockingFLowDBService(blockingFlowRepository);
      blockingflowdbservice.LoadFlow(blockingflow);

      System.out.println(blockingflowdbservice.findAll2());
        System.out.println(blockingflowdbservice.findall3());

        //        blockingFlowRepository.save(flow);
 //       blockingflowdbservice.LoadFlow(new BlockingFlow(flow.getId(),flow.getTable_id(),flow.getHardtimeout(),flow.getPriority(),match.getIpv4destination(),flow.getFlowname()));


 //       FlowRepository repository = null;
//        repository.save(flow);
//        FlowDataBaseLoad  load = new FlowDataBaseLoad(repository);
//        repository.save(flow);
          //  repository.save(flow);

///        FlowDBservice savedflow = new FlowDBservice();
   ////     savedflow.Update(flow);
        //persist stuff

//        flowrepository.save(flow);

        //create object mapper jackson
        ObjectMapper mapper = new ObjectMapper();
        //configure mapper
        //mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,true); //put root class name
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
       // mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,true);

        //View JSON
        System.out.println("JSon from java =>" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inventoryFLows));



//        http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/openflow:246321139094487/flow-node-inventory:table/0
        RestTemplate restTemplate = new RestTemplate();

        String plainCreds = "admin:admin";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization","Basic " + base64Creds);


    //print manual jackson json generated POJO
       // System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arraynode));

        String url;
        url = "http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/" + nodeID + "/flow-node-inventory:table/" + tableID;


        HttpEntity<InventoryFlows> entity = new HttpEntity<InventoryFlows>(inventoryFLows,headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println("GET Nodes");
        System.out.println(url);
        System.out.println("CREATE: Flow=" + flowID + " Resp=" + response.getStatusCode());
//        ResponseEntity<String> reply = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        // ResponseEntity<String> reply = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//        reply = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
     //   System.out.println("Response: "+reply.getStatusCode());
  //      System.out.println("body:"+ reply.getBody()  );



    }

    public void CreateFlowTest(String nodeID, Integer tableID, String flowID) throws JsonProcessingException {
        System.out.println("Flow create");

        Match match = new Match();
        match.setIpv4destination("10.11.11.118/32");

        EtherMatch ethernetmatch = new EtherMatch(new EtherType(2048));
        match.setEthernetmatch(ethernetmatch);

        Action action = new Action();
        action.setOrder(1);
        action.setDropaction(new HashMap<>());

        ApplyActions applyactions = new ApplyActions();
        applyactions.setAction(Lists.newArrayList(action));

        Instruction instruction = new Instruction();
        instruction.setOrder(1);
        instruction.setApplyactions(applyactions);

        Instructions instructions = new Instructions();
        instructions.setInstruction(Lists.newArrayList(instruction));

        Flow flow = new Flow();
        flow.setId("555");
        flow.setInstallHw(false);
        flow.setTable_id(0);
        flow.setStrict(true);
        flow.setMatch(match);
        flow.setIdletimeout(0);
        flow.setFlowname("myflow555hehe");
        flow.setBarrier(false);
        flow.setCookie_mask(555);
        flow.setCookie(4);
        flow.setPriority(2);
        flow.setInstructions(instructions);

        InventoryFlows inventoryFLows = new InventoryFlows();
        inventoryFLows.setFlows(Lists.newArrayList(flow));

        //Map<String, List<Flow>> wrappedFlow = Maps.newHashMap();
        //wrappedFlow.put("flow-node-inventory:flow", Collections.singletonList(flow));




        //etc

        //create object mapper jackson

        ObjectMapper mapper = new ObjectMapper();
        //configure mapper
        //mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,true); //put root class name
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,true);



        System.out.println("json from java");
        String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inventoryFLows);
        // String output1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrappedFlow);
        //print my java object as json
        System.out.println(output);
        //mapper.readValue(output, )


        // System.out.println(getObjectMapper().writeValueAsString(flow));

//        http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/openflow:246321139094487/flow-node-inventory:table/0
        RestTemplate restTemplate = new RestTemplate();

        String plainCreds = "admin:admin";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization","Basic " + base64Creds);

        //create body    TEST POJO FROM JACKSON
        ArrayNode arraynode = mapper.createArrayNode();
        com.fasterxml.jackson.databind.node.ObjectNode postData0 = mapper.createObjectNode();

//       JSONObject postData = new JSONObject();
        com.fasterxml.jackson.databind.node.ObjectNode postData = mapper.createObjectNode();
        postData.put("id","555");
        postData.put("installHw","false");
        postData.put("table_id","0");
        postData.put("strict","true");

        ArrayNode matchdataArr = mapper.createArrayNode();
//        com.fasterxml.jackson.databind.node.ObjectNode matchpath = mapper.createObjectNode();
//        matchpath.path("match");

        //match POJO

        //match ethernetmach type
        com.fasterxml.jackson.databind.node.ObjectNode ethernetMatchType = mapper.createObjectNode();
        ethernetMatchType.put("type","2048");
        //ethernet-match POJO
        com.fasterxml.jackson.databind.node.ObjectNode ethernetMatchPojo = mapper.createObjectNode();
        ethernetMatchPojo.putPOJO("ethernet-type",ethernetMatchType);
        //match
        //     com.fasterxml.jackson.databind.node.ObjectNode matchData = mapper.createObjectNode();
        com.fasterxml.jackson.databind.node.ObjectNode matchPojo = mapper.createObjectNode();
        matchPojo.put("ipv4-destination","10.11.11.118/32");
        matchPojo.putPOJO("ethernet-match",ethernetMatchPojo);
        //     matchData.put("ipv4-destination","10.11.11.118/32");
        //       matchdataArr.add(matchData);

        ObjectNode postData2 = mapper.createObjectNode();
        postData2.putPOJO("match", matchPojo);


        arraynode.add(postData0);
        arraynode.add(postData);
        arraynode.add(postData2);
//        System.out.println("json::");


        //print manual jackson json generated POJO
        // System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arraynode));

        String url;
        url = "http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/" + nodeID + "/flow-node-inventory:table/" + tableID;

//        ResponseEntity<String> reply;
//        ResponseEntity<String> respnse;

        HttpEntity<InventoryFlows> request = new HttpEntity<InventoryFlows>(inventoryFLows,headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        System.out.println("GET Nodes");
        System.out.println(url);
        System.out.println(response.getStatusCode());
//        ResponseEntity<String> reply = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        // ResponseEntity<String> reply = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//        reply = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        //   System.out.println("Response: "+reply.getStatusCode());
        //      System.out.println("body:"+ reply.getBody()  );



    }

    public void UpdateFlow(){
        System.out.println("Flow Update");
    }

    public void DeleteFlow(String nodeID, Integer tableID, String flowID){
        System.out.println("Flow Delete");
        String url;
        url = "http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/" + nodeID + "/flow-node-inventory:table/" + tableID + "/flow/" + flowID;

        RestTemplate restTemplate = new RestTemplate();

        String plainCreds = "admin:admin";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization","Basic " + base64Creds);

        HttpEntity<InventoryFlows> request = new HttpEntity<InventoryFlows>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
            System.out.println("DELETE: Flow=" + flowID + " Resp=" + response.getStatusCode());
        }
        catch (HttpClientErrorException | HttpServerErrorException httpEx){
            if (httpEx.getRawStatusCode() == 404) {
                System.out.println("CHECK SDN If DOWN");
            }

        }
    }
}
