package TestHelperUtils

// this utility functions do the dirty work of getting the system
// property (specified as -Dkey=value at the command line) and 
// return
// set as follows: "set JAVA_OPTS=-D<key1>=<value1> -D<key2>=<value2> ... "
// NOTE: do not add double quotes to the value of JAVA_OPTS
object MageUtils {
	def getSysPropAsStr(sysPropName: String, default: String): String=
	{
		val prop=System.getProperty(sysPropName)
		println("Prop " + sysPropName + " " + prop)
		if (prop==null)
			default
		else
			prop
	}
	
	def getRandInt(start: Int, end: Int): Int=
	{
		val rn=new scala.util.Random
		val rtn=rn.nextInt(end-start)+start
		rtn
	}
	
	def shouldUseFormKey(): Boolean = {
		val useFormKey = getSysPropAsStr("useFormKey");
		(useFormKey!=null && "true".equalsIgnoreCase(useFormKey))
	}
}