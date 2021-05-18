package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.PackagePrivate;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@PackagePrivate
public class BotUser {
    Integer step;
    String name;
    int age;
    String phone;
    Contact contact;
    ArrayList<String> disease;

    ArrayList<String> arrayList = new ArrayList<>();

    public void setStep(Integer step) {
        this.step = step;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDisease(String text) {
        arrayList.add(text);
        this.disease = arrayList;
    }

    public String getIllness() {

        String res = "";
        String res2 = "";
        if (getDisease().size()> 1) {
            res = "\nОлдинги мурожаатлар:\n";
            for (int i = 0; i < getDisease().size()-1; i++) {
                res+=i+1+"."+getDisease().get(i)+"\n";
            }
            res += "\nОхирги мурожаат: " + getDisease().get(getDisease().size()-1);
            res2="\nОлдинги мурожаатлар:\n"+getDisease().get(getDisease().size()-2)+"\nОхирги мурожаат:\n" + getDisease().get(getDisease().size()-1);
        } else {
            res += getDisease().get(0);
        }
        return res.length()>3750?res2:res;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
