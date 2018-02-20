package topology.description.actual;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import commonTool.CommonTool;

public abstract class VM {

    private static final Logger logger = Logger.getLogger(VM.class);

    public String name;
    public String type;
    public String nodeType;
    public String CPU;
    public String Mem;
    public String OStype;

    //@JsonIgnore
    public String defaultSSHAccount;

    //Currently, the SIDE subsystem uses this field for GUI.
    //This script defines for individual VM.
    //It's important to keep in mind that this is only the script path.
    //The real content in the field of 'v_scriptString'
    public String script;

    /**
     * Indicate the real content of the script. Examples:
     * url@http://www.mydomain.com/pathToFile/myId_dsa or file@/home/id_dsa or
     * name@test.sh or null (the file path is absolute path). This is not case
     * sensitive. The file must be exists. Otherwise, there will be a warning.
     */
    @JsonIgnore
    public String v_scriptString;


    //Do not need to be the same with the node name any more.
    //The initial value should be "null", which means the public is not determined. 
    //When the status of the sub-topology is stopped, deleted or failed, this field should also be "null".
    public String publicAddress;

    
    /**
     *  This records all the connections to this VM. It is useful,
     *  because there is some time that no connection is setup. For example, 
     *  the tunnel is not set. Then the VM's private IP address of this connection 
     *  should still can be connected! This is especially for the top connections. 
     *  The keys are all the private IPs of this VM. Boolean tells whether the self 
     *  eth tunnel port has been setup. It is initialized as TRUE.
     */
    @JsonIgnore
    public Map<String, Boolean> selfEthAddresses;
    
    
    /**
     * Specify the V-Engine Class to handle this VM. Useful for extension to currently unsupported VM OS types 
     * or some user-defined operations.
     * If it is null, than V-Engine for current supported VMs will be loaded by default.
     */
    public String VEngineClass;
    
    
    /**
     * Some extra key-value information needed for some specified Cloud. 
     * This is useful for extension.
     */
	public Map<String, String> extraInfo;
	
	/**
	 * This is a list to store all the connectors that attached on 
	 * this VM.
	 */
	@JsonIgnore
	public ArrayList<ActualConnectionPoint> vmConnectors;
	
	/**
	 * This is a pointer pointing back to the sub-topology 
	 * which this VM belongs to.
	 */
	@JsonIgnore
	public SubTopologyInfo ponintBack2STI;
	
	/**
	 * This is a number indicate the connection technique. 
	 * Default value is 0, if it is null.
	 * Reserved for future.
	 */
	public String VNFType;
	
	/**
	 * Tells the node name that this VM scales from. If it is 'null',
	 * it means this is no scaled.
	 */
	public String scaledFrom;

    /**
     * Load script content from 'script' of current VM.
     */
    public boolean loadScript(String currentDir) {
        if (script == null) {
            logger.warn("Please configure the script path first!");
            return false;
        }

        if ((v_scriptString = CommonTool.getFileContent(script, currentDir)) == null) {
            logger.error("File of script cannot be loaded!");
            return false;
        }

        return true;
    }

}