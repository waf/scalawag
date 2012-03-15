package bot
import java.net.URLClassLoader
import java.net.URL
import java.io.File
import scala.util.matching.Regex

object ModuleLoader {

  def getModules() : List[Module] = {
    
    // search our output directory for modules
    val directory = new File("target/")
    val pkg = "modules"
    val moduleFilePattern = (pkg + File.separator + "[A-Za-z].*.class").r
    val files = recursiveFindFiles(directory, moduleFilePattern)
    
    if(files.length == 0)
      return List[Module]()
    
    // get a reference to the Class object for each module, so we can instantiate it
    val classes = for(
      file <- files;
      val classname = pkg + "." + file.getName.dropRight(".class".length)
    ) yield Class.forName(classname)
    
    // make sure the file really is a module
    val moduleClasses = classes.filter(_.getInterfaces().contains(classOf[Module]))

    // instantiate and return our modules
    moduleClasses.map(_.newInstance).toList.asInstanceOf[List[Module]]
  }
  
  def recursiveFindFiles(directory: File, r: Regex): Array[File] = {
    val contents = directory.listFiles
    val matches = contents.filter(f => !f.isDirectory && r.findFirstIn(f.getPath).isDefined)
    matches ++ contents.filter(_.isDirectory).flatMap(recursiveFindFiles(_, r))
  }
}