package com.cerner.shipit.DynaSites.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cerner.shipit.DynaSites.dao.DynaSitesRepository;
import com.cerner.shipit.DynaSites.model.DynaSitesModel;
import com.cerner.shipit.DynaSites.model.SiteTemplateModel;
import com.cerner.shipit.DynaSites.util.DynaSiteAppUtils;
import com.mongodb.client.result.UpdateResult;

@Controller
@RequestMapping("/dynasites")
@CrossOrigin("*")
public class DynaSitesController {
	@Autowired
    DynaSitesRepository dynaSitesRepository;
	
	@Autowired
    private ServletContext servletContext;
 
	//private static final String DYNASITE_TEMPLATE_PATH= "/static/DynaSites_Template.xlsx";
	private static final String DYNASITE_TEMPLATE_PATH= "C:\\Dinesh\\ProjectUtils\\ShipIt\\DynoSites_brainstroming\\DynaSites_Template.xlsx";
	
	MongoTemplate mongoTemplate;
	public DynaSitesController() {
		super();
	}
	
	@GetMapping("/")
	public String loadIndex(Model mod) {
		
		mod.addAttribute("indexString", "Dyna Sites Application");
		return "index";
	}
	@GetMapping("/default")
	public String getDefault(Model mod) {
		
		mod.addAttribute("myString", "Test Application");
		return "default";
	}
	
    @GetMapping("/allsites")
    public String getAllDynaSites(Model mod) {
        Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdAt");
         mod.addAttribute("DynaSitesList", dynaSitesRepository.findAll(sortByCreatedAtDesc));
         return "listView";
    }

    @PostMapping("/saveapp")
    public String createDynaSites(@Valid @RequestBody DynaSitesModel dynaSites) {
        dynaSites.setCompleted(false);
        dynaSitesRepository.save(dynaSites);
        return "default";
    }

    @GetMapping(value="/sites/{id}")
    public ResponseEntity<DynaSitesModel> getDynaSitesById(@PathVariable("id") String id) {
        return dynaSitesRepository.findById(id)
                .map(dynaSites -> ResponseEntity.ok().body(dynaSites))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value="/sites/{id}")
    public ResponseEntity<DynaSitesModel> updateDynaSites(@PathVariable("id") String id,
                                           @Valid @RequestBody DynaSitesModel dynaSites) {
        return dynaSitesRepository.findById(id)
                .map(dynaSitesData -> {
                    dynaSitesData.setTitle(dynaSites.getTitle());
                    dynaSitesData.setCompleted(dynaSites.getCompleted());
                    DynaSitesModel updatedDynaSites = dynaSitesRepository.save(dynaSitesData);
                    return ResponseEntity.ok().body(updatedDynaSites);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value="/sites/{id}")
    public ResponseEntity<?> deleteDynaSites(@PathVariable("id") String id) {
        return dynaSitesRepository.findById(id)
                .map(dynaSites -> {
                    dynaSitesRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile multipart, @RequestParam("email") String email) {
    	//mongoTemplate = dynaSitesRepository.mongoTemplate;
    	
        try {
        	SiteTemplateModel demoDocument = new SiteTemplateModel();
            /*demoDocument.setEmailId(email);
            demoDocument.setDocType("pictures");
            demoDocument.setDocument(new Binary(BsonBinarySubType.BINARY, multipart.getBytes()));
            mongoTemplate.insert(demoDocument);*/
            System.out.println(demoDocument);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        return "success";
    }
    
    // http://localhost:8080/download1?fileName=abc.zip
    // Using ResponseEntity<InputStreamResource>
    @RequestMapping("/downloadTemplate")
    public ResponseEntity<InputStreamResource> downloadFile1() throws IOException {
 
        MediaType mediaType = DynaSiteAppUtils.getMediaTypeForFileName(this.servletContext, DYNASITE_TEMPLATE_PATH);
        System.out.println("fileName: " + DYNASITE_TEMPLATE_PATH);
        System.out.println("mediaType: " + mediaType);
 
        File file = new File(DYNASITE_TEMPLATE_PATH);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
 
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }
    
    @PutMapping("/update")
    public long updateDomain(String domain, boolean displayAds) {
    	//mongoTemplate = dynaSitesRepository.mongoTemplate;
    	
        Query query = new Query(Criteria.where("domain").is(domain));
        Update update = new Update();
        update.set("displayAds", displayAds);

        UpdateResult result = mongoTemplate.updateFirst(query, update, SiteTemplateModel.class);

        if(result!=null)
            return result.getModifiedCount();
        else
            return 0;

    }
    
    @PostMapping("/retrieve")
    public String retrieveFile(@RequestParam("email") String email){
    	//mongoTemplate = dynaSitesRepository.mongoTemplate;
    	/*SiteTemplateModel demoDocument = mongoTemplate.findOne(new BasicQuery("{emailId : \""+email+"\", docType : \"pictures\"}"), SiteTemplateModel.class);;
        System.out.println(demoDocument);
        Binary document = demoDocument.getDocument();
        if(document != null) {
            FileOutputStream fileOuputStream = null;
            try {
                fileOuputStream = new FileOutputStream(RETRIEVE_FOLDER + "prof_pic.jpg");
                fileOuputStream.write(document.getData());
            } catch (Exception e) {
                e.printStackTrace();
                return "failure";
            } finally {
                if (fileOuputStream != null) {
                    try {
                        fileOuputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "failure";
                    }
                }
            }
        }*/
        //return "redirect:/dynaSites/" + demoDocument.getId();
    	return null;
    }
}



