// package com.etz.gh.service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;

// import javax.sql.DataSource;

// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.etz.gh.DTO.ConfigDTO;
// import com.etz.gh.DTO.ConfigUpdateDTO;
// import com.etz.gh.model.Config;
// import com.etz.gh.repository.ConfigRepository;
// import com.etz.gh.repository.PrivilegeRepository;

// import lombok.RequiredArgsConstructor;



// @Service
// @Transactional
// @RequiredArgsConstructor
// public class ConfigService {

	
// 	@Autowired
// 	private  ConfigRepository confgRepository;
	

	
// 	//@Qualifier("dataSource")

// 	@Autowired
// 	private DataSource dataSource;
	
//     private final ModelMapper modelMapper;

	
// 	@Autowired
// 	private JdbcTemplate jdbcTemplate;
	
// 	public void setDataSource(DataSource dataSource) {
// 		this.dataSource = dataSource;
// 		this.jdbcTemplate = new JdbcTemplate(dataSource);
// 	}

	
// //	@Transactional(readOnly = true)
// //	public List<Config> findAllconfgs() {
// //		List<Config> confg = confgRepository.findAll();
// //		return confg;
// //	}
	
// 	public Config findByKeyName(String name) {
		
// 		return confgRepository.findByKeyName(name);
		
// 	}
	
// 	public Config addconfg(Config confg) {
		
// 		return confgRepository.save(confg);
	
// 	}
    
 
    
//     public void updateconfg(Config confg) {

//     	confg.setUpdateDate(LocalDateTime.now());
    	
//     	confgRepository.save(confg);
    	  	
//     }
    
//     public Optional<Config> findDeptById(Long id) {
    	
// 		return confgRepository.findById(id);
     	  	
//     }
    
    
//     public Config addconfig(ConfigDTO configDTO) {

	       
	       
//         Config config=new Config();
     
//         modelMapper.map(configDTO, config);
        
   

// 	    return confgRepository.save(config);
	
// 	}
   
//    public void deleteconfig(Long id) {
   	
//    	confgRepository.deleteById(id);
   	
//    }
   
// //   public void updateappCred(AppCredDTO rol, Long appCredId) {
// //   
// //      rol.setUpdateDate(LocalDateTime.now());
// ////      
// //      rol.setCreateDate(appCred.getCreateDate());
// //      
// //      rol.setCreateBy(appCred.getCreateBy());
// //     
// //
// //      modelMapper.map(rol, appCred);
// //
// //      appCredRepository.save(appCred);
// //   	  	
// //   }
   
// //   public void updateWhitelist(WhitelistDTO rol, Long whitelistId) {
// //   	//modelMapper = new ModelMapper();
// //   	
// //   
//    public void updateConfig(ConfigUpdateDTO rol, Long configId) {
//    	//modelMapper = new ModelMapper();
   	    
//       Optional<Config> config=confgRepository.findById(configId);
//       System.out.println("Config:: "+config);
     
// //      rol.setAppCred(whitelistRepository.findByIpValue(appId));
//      // rol.setAppCred(confgRepository.findByvalue(appId));
//       rol.setUpdateDate(LocalDateTime.now());

//       modelMapper.map(rol, config.get());

//       confgRepository.save(config.get());
   	  	
//    }
   
//    public Optional<Config> findById(Long logoutId) {
//    	//appCredRepository2.f
// 		return confgRepository.findById(logoutId);
    	  	
//    }


// //	public Config findByAppId(String appId) {
// //		
// //
// //		AppCred appCred=appCredRepository.findByName(appId);
// //		
// //		return confgRepository.findByAppCred(appCred);
// //		
// //	}


// 	public List<Config> getRecsBySpecifiedParams(String startDate, String enDate, String appId) {
// 		return null;
// 	}


// 	public String getCountRecsBySpecifiedParams(String startDate, String enDate, String appId) {
// 		return null;
// 	}


// 	public List<Config> getConfigWithLimit(String appId, Object object, int i) {
// 		return null;
// 	}
    

    
    
   
// }
