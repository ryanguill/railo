package railo.runtime.util;

import railo.commons.io.res.Resource;
import railo.commons.io.res.util.ResourceUtil;
import railo.commons.lang.StringUtil;
import railo.runtime.Component;
import railo.runtime.ComponentWrap;
import railo.runtime.Mapping;
import railo.runtime.PageContext;
import railo.runtime.config.Config;
import railo.runtime.config.ConfigImpl;
import railo.runtime.exp.ApplicationException;
import railo.runtime.exp.PageException;
import railo.runtime.listener.ApplicationContextUtil;
import railo.runtime.net.s3.Properties;
import railo.runtime.op.Caster;
import railo.runtime.orm.ORMConfiguration;
import railo.runtime.type.Collection;
import railo.runtime.type.KeyImpl;
import railo.runtime.type.Scope;
import railo.runtime.type.Struct;
import railo.runtime.type.dt.TimeSpan;

/**
 * 
 */
public class ApplicationContextImpl implements ApplicationContextPro {
   

	private static final long serialVersionUID = 940663152793150953L;

	private static final Collection.Key ACCESS_KEY_ID = KeyImpl.getInstance("accessKeyId");
	private static final Collection.Key AWS_SECRET_KEY = KeyImpl.getInstance("awsSecretKey");
	private static final Collection.Key DEFAULT_LOCATION = KeyImpl.getInstance("defaultLocation");
	private static final Collection.Key HOST = KeyImpl.getInstance("host");
	private static final Collection.Key SERVER = KeyImpl.getInstance("server");
	private static final Collection.Key DATA_SOURCE = KeyImpl.getInstance("datasource");
	
	

	private String name;
    private boolean setClientCookies;
    private boolean setDomainCookies;
    private boolean setSessionManagement;
    private boolean setClientManagement;
    private TimeSpan sessionTimeout=null; 
	private TimeSpan clientTimeout;
    private TimeSpan applicationTimeout=null;
    private int loginStorage=-1;
    private String clientstorage;
    private String sessionstorage;
	private int scriptProtect;
	private Mapping[] mappings;
	private Mapping[] ctmappings;
	private Mapping[] cmappings;
	private boolean secureJson;
	private String secureJsonPrefix="//";
	private boolean isDefault;
	private String defaultDataSource;
	private boolean ormEnabled;
	private String ormdatasource;
	private ORMConfiguration config;
	private Component component;
	private Properties s3;
	private String applicationtoken;
	private String cookiedomain;
	private int idletimeout=1800;


	private int localMode;
	private short sessionType;
    private boolean sessionCluster;
    private boolean clientCluster;

    
    /**
     * constructor of the class
     * @param config
     */
    public ApplicationContextImpl(Config config, Component component,boolean isDefault) {
    	ConfigImpl ci = ((ConfigImpl)config);
    	this.component=component;
    	setClientCookies=config.isClientCookies();
        setDomainCookies=config.isDomainCookies();
        setSessionManagement=config.isSessionManagement();
        setClientManagement=config.isClientManagement();
        sessionTimeout=config.getSessionTimeout();
        clientTimeout=ci.getClientTimeout();
        applicationTimeout=config.getApplicationTimeout();
        loginStorage=Scope.SCOPE_COOKIE;
        scriptProtect=config.getScriptProtect();
        this.isDefault=isDefault;
        this.defaultDataSource=ci.getDefaultDataSource();
        this.localMode=config.getLocalMode();
        this.sessionType=config.getSessionType();
        this.sessionCluster=ci.getSessionCluster();
        this.clientCluster=ci.getClientCluster();
    }


	public ApplicationContextImpl(Config config, boolean isDefault) {
    	this(config,null,isDefault);
    }
    
    /**
     * Constructor of the class, only used by duplicate method
     */
    private ApplicationContextImpl() {
    	
    }
    

	public ApplicationContext duplicate() {
		ApplicationContextImpl dbl = new ApplicationContextImpl();
		dbl.name=name;
		dbl.setClientCookies=setClientCookies;
		dbl.setDomainCookies=setDomainCookies;
		dbl.setSessionManagement=setSessionManagement;
		dbl.setClientManagement=setClientManagement;
		dbl.sessionTimeout=sessionTimeout;
		dbl.clientTimeout=clientTimeout;
		dbl.applicationTimeout=applicationTimeout;
		dbl.loginStorage=loginStorage;
		dbl.clientstorage=clientstorage;
		dbl.sessionstorage=sessionstorage;
		dbl.scriptProtect=scriptProtect;
		dbl.mappings=mappings;
		dbl.ctmappings=ctmappings;
		dbl.cmappings=cmappings;
		dbl.secureJson=secureJson;
		dbl.secureJsonPrefix=secureJsonPrefix;
		dbl.isDefault=isDefault;
		dbl.defaultDataSource=defaultDataSource;
		dbl.applicationtoken=applicationtoken;
		dbl.cookiedomain=cookiedomain;
		dbl.idletimeout=idletimeout;
		dbl.localMode=localMode;
		dbl.sessionType=sessionType;
		
		dbl.ormEnabled=ormEnabled;
		dbl.config=config;
		dbl.ormdatasource=ormdatasource;
		dbl.sessionCluster=sessionCluster;
		dbl.clientCluster=clientCluster;
		
		return dbl;
	}
    
    
    /**
     * @see railo.runtime.util.IApplicationContext#getApplicationTimeout()
     */
    public TimeSpan getApplicationTimeout() {
        return applicationTimeout;
    }
    /**
     * @param applicationTimeout The applicationTimeout to set.
     */
    public void setApplicationTimeout(TimeSpan applicationTimeout) {
        this.applicationTimeout = applicationTimeout;
    }
    /**
     * @see railo.runtime.util.IApplicationContext#getLoginStorage()
     */
    public int getLoginStorage() {
        return loginStorage;
    }
    /**
     * @param loginStorage The loginStorage to set.
     */
    public void setLoginStorage(int loginStorage) {
        this.loginStorage = loginStorage;
    }
    
    public void setLoginStorage(String strLoginStorage) throws ApplicationException {
    	strLoginStorage=strLoginStorage.toLowerCase().trim();
        if(strLoginStorage.equals("session"))setLoginStorage(Scope.SCOPE_SESSION);
        else if(strLoginStorage.equals("cookie"))setLoginStorage(Scope.SCOPE_COOKIE);
        else throw new ApplicationException("invalid loginStorage definition ["+strLoginStorage+"], valid values are [session,cookie]");
    }
    
    
    
    /**
     * @see railo.runtime.util.IApplicationContext#getFullName()
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @see railo.runtime.util.IApplicationContext#getSessionTimeout()
     */
    public TimeSpan getSessionTimeout() {
        return sessionTimeout;
    }
    
    /**
     * @param sessionTimeout The sessionTimeout to set.
     */
    public void setSessionTimeout(TimeSpan sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }


    public TimeSpan getClientTimeout() {
        return clientTimeout;
    }
    
    /**
     * @param sessionTimeout The sessionTimeout to set.
     */
    public void setClientTimeout(TimeSpan clientTimeout) {
        this.clientTimeout = clientTimeout;
    }
    
    /**
     * @see railo.runtime.util.IApplicationContext#isSetClientCookies()
     */
    public boolean isSetClientCookies() {
        return setClientCookies;
    }
    /**
     * @param setClientCookies The setClientCookies to set.
     */
    public void setSetClientCookies(boolean setClientCookies) {
        this.setClientCookies = setClientCookies;
    }
    /**
     * @see railo.runtime.util.IApplicationContext#isSetClientManagement()
     */
    public boolean isSetClientManagement() {
        return setClientManagement;
    }
    /**
     * @param setClientManagement The setClientManagement to set.
     */
    public void setSetClientManagement(boolean setClientManagement) {
        this.setClientManagement = setClientManagement;
    }
    /**
     * @see railo.runtime.util.IApplicationContext#isSetDomainCookies()
     */
    public boolean isSetDomainCookies() {
        return setDomainCookies;
    }
    /**
     * @param setDomainCookies The setDomainCookies to set.
     */
    public void setSetDomainCookies(boolean setDomainCookies) {
        this.setDomainCookies = setDomainCookies;
    }
    /**
     * @see railo.runtime.util.IApplicationContext#isSetSessionManagement()
     */
    public boolean isSetSessionManagement() {
        return setSessionManagement;
    }
    /**
     * @param setSessionManagement The setSessionManagement to set.
     */
    public void setSetSessionManagement(boolean setSessionManagement) {
        this.setSessionManagement = setSessionManagement;
    }
    /**
     * @see railo.runtime.util.IApplicationContext#getClientstorage()
     */
    public String getClientstorage() {
        return clientstorage;
    }
    public String getSessionstorage() {
        return sessionstorage;
    }
    /**
     * @param clientstorage The clientstorage to set.
     */
    public void setClientstorage(String clientstorage) {
        this.clientstorage = clientstorage;
    }
    public void setSessionstorage(String sessionstorage) {
        this.sessionstorage = sessionstorage;
    }

    /**
     * @see railo.runtime.util.IApplicationContext#hasName()
     */
    public boolean hasName() {
        return name!=null;
    }

    /**
     * @param scriptProtect The scriptProtect to set.
     */
    public void setScriptProtect(String strScriptProtect) {
		this.scriptProtect=ApplicationContextUtil.translateScriptProtect(strScriptProtect);
	}
    
    /**
     * @param scriptProtect The scriptProtect to set.
     */
    public void setScriptProtect(int scriptProtect) {
		this.scriptProtect=scriptProtect;
	}

	/**
	 * @see railo.runtime.util.ApplicationContext#getScriptProtect()
	 */
	public int getScriptProtect() {
		//if(isDefault)print.err("get:"+scriptProtect);
		return scriptProtect;
	}

	


	public void setMappings(Mapping[] mappings) {
		if(mappings.length>0)this.mappings=mappings;
	}

	/**
	 * @return the mappings
	 */
	public Mapping[] getMappings() {
		return mappings;
	}

	public void setCustomTagMappings(Mapping[] ctmappings) {
		this.ctmappings=ctmappings;
	}

	public Mapping[] getCustomTagMappings() {
		return ctmappings;
	}

	public void setComponentMappings(Mapping[] cmappings) {
		this.cmappings=cmappings;
	}

	public Mapping[] getComponentMappings() {
		return cmappings;
	}

	public void setSecureJson(boolean secureJson) {
		this.secureJson=secureJson;
	}
	
	public void setSecureJsonPrefix(String secureJsonPrefix) {
		this.secureJsonPrefix=secureJsonPrefix;
	}

	/**
	 * @return the secureJson
	 */
	public boolean getSecureJson() {
		return secureJson;
	}

	/**
	 * @return the secureJsonPrefix
	 */
	public String getSecureJsonPrefix() {
		return secureJsonPrefix;
	}

	 /**
	 * @return the defaultDataSource
	 */
	public String getDefaultDataSource() {
		return defaultDataSource;
	}

	/**
	 * @param defaultDataSource the defaultDataSource to set
	 */
	public void setDefaultDataSource(String defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}
	
	public void setORMDataSource(String ormdatasource) {
		this.ormdatasource = ormdatasource;
	}

	public boolean isORMEnabled() {
		return ormEnabled;
	}

	public String getORMDatasource() {
		return ormdatasource;
	}

	public ORMConfiguration getORMConfiguration() {
		return config;
	}
	public void setORMConfiguration(ORMConfiguration config) {
		this.config= config;
	}
	
	public void setORMConfiguration(PageContext pc,Struct sct) throws PageException {
		Resource res=ResourceUtil.getResource(pc, pc.getCurrentTemplatePageSource()).getParentResource();
		ConfigImpl config=(ConfigImpl) pc.getConfig();
		ORMConfiguration ormConfig=ORMConfiguration.load(config,sct,res,config.getORMConfig());
		setORMConfiguration(ormConfig);
		
		// datasource
		Object o = sct.get(DATA_SOURCE,null);
		if(o!=null) setORMDataSource(Caster.toString(o));
	}

	public void setORMEnabled(boolean ormEnabled) {
		this.ormEnabled=ormEnabled;
	}

	/**
	 * @see railo.runtime.util.ApplicationContextPro#getComponent()
	 */
	public Component getComponent() {
		return component;
	}

	public void setS3(Struct sct) throws PageException {
		String host=Caster.toString(sct.get(HOST,null));
		if(StringUtil.isEmpty(host))host=Caster.toString(sct.get(SERVER,null));
		
		setS3(
				Caster.toString(sct.get(ACCESS_KEY_ID,null)),
				Caster.toString(sct.get(AWS_SECRET_KEY,null)),
				Caster.toString(sct.get(DEFAULT_LOCATION,null)),
				host
			);
	}

	public void setS3(String accessKeyId, String awsSecretKey, String defaultLocation, String host) {
		this.s3=new Properties();
		if(!StringUtil.isEmpty(accessKeyId))s3.setAccessKeyId(accessKeyId);
		if(!StringUtil.isEmpty(awsSecretKey))s3.setSecretAccessKey(awsSecretKey);
		if(!StringUtil.isEmpty(defaultLocation))s3.setDefaultLocation(defaultLocation);
		if(!StringUtil.isEmpty(host))s3.setHost(host);
	}


	/**
	 * @return the s3
	 */
	public Properties getS3() {
		if(s3==null) s3=new Properties();
		return s3;
	}


	public void setSecuritySettings(String applicationtoken, String cookiedomain, int idletimeout) {
		this.applicationtoken=applicationtoken;
		this.cookiedomain=cookiedomain;
		this.idletimeout=idletimeout;
		
	}
	public String getSecurityApplicationToken() {
		if(StringUtil.isEmpty(applicationtoken,true)) return getName();
		return applicationtoken;
	}
	public String getSecurityCookieDomain() {
		if(StringUtil.isEmpty(applicationtoken,true)) return null;
		return cookiedomain;
	}
	public int getSecurityIdleTimeout() {
		if(idletimeout<1) return 1800;
		return idletimeout;
	}


	/**
	 * @return the localMode
	 */
	public int getLocalMode() {
		return localMode;
	}


	/**
	 * @param localMode the localMode to set
	 */
	public void setLocalMode(int localMode) {
		this.localMode = localMode;
	}



    /**
	 * @return the sessionType
	 */
	public short getSessionType() {
		return sessionType;
	}

    /**
	 * @return the sessionType
	 */
	public void setSessionType(short sessionType) {
		this.sessionType= sessionType;
	}


	/**
	 * @return the sessionCluster
	 */
	public boolean getSessionCluster() {
		return sessionCluster;
	}


	/**
	 * @param sessionCluster the sessionCluster to set
	 */
	public void setSessionCluster(boolean sessionCluster) {
		this.sessionCluster = sessionCluster;
	}


	/**
	 * @return the clientCluster
	 */
	public boolean getClientCluster() {
		return clientCluster;
	}


	/**
	 * @param clientCluster the clientCluster to set
	 */
	public void setClientCluster(boolean clientCluster) {
		this.clientCluster = clientCluster;
	}
	
	/*
	 * @see railo.runtime.util.ApplicationContextPro#getCustom(railo.runtime.type.Collection.Key)
	 */
	public Object getCustom(Collection.Key key) {
		Component cfc = getComponent();
		if(cfc!=null){
			try {
				ComponentWrap cw=ComponentWrap.toComponentWrap(Component.ACCESS_PRIVATE, cfc);
				return cw.get(key,null);
			} 
			catch (PageException e) {}
		}
		return null;
	}
}