package cn.snnyyp.project.authlibinjectorwrapper;

import java.util.List;


public class ConfigStruct {

    boolean print_welcome_title;
    boolean print_system_detail;
    OverrideType override_type;
    List<String> override_launch_command;
    String override_launch_script_path;
    enum OverrideType {
        command,
        script
    }
}
