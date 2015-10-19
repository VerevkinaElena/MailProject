import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.*;

/**
 * Created by ����� on 19.10.2015.
 */
public class Mail {
    /**
     * Created by ����� on 18.10.2015.
     */
        /*
    ���������: ��������, ������� ����� ��������� �� �����.
    � ����� �������� ����� �������� �� ���� � ���� ������������ ������.
    */
        public static interface Sendable {
            String getFrom();
            String getTo();
        }

        /*
    ����������� �����,������� ��������� �������������� ������ ��������
    ��������� � ���������� ������ � ��������������� ����� ������.
    */
        public static abstract class AbstractSendable implements Sendable {

            protected final String from;
            protected final String to;

            public AbstractSendable(String from, String to) {
                this.from = from;
                this.to = to;
            }

            @Override
            public String getFrom() {
                return from;
            }

            @Override
            public String getTo() {
                return to;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                AbstractSendable that = (AbstractSendable) o;

                if (!from.equals(that.from)) return false;
                if (!to.equals(that.to)) return false;

                return true;
            }

        }

        /*
    ������, � �������� ���� �����, ������� ����� �������� � ������� ������ `getMessage`
    */
        public static class MailMessage extends AbstractSendable {
            private final String message;

            public MailMessage(String from, String to, String message) {
                super(from, to);
                this.message = message;
            }

            public String getMessage() {
                return message;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                if (!super.equals(o)) return false;

                MailMessage that = (MailMessage) o;

                if (message != null ? !message.equals(that.message) : that.message != null) return false;

                return true;
            }

        }

        /*
    �������, ���������� ������� ����� �������� � ������� ������ `getContent`
    */
        public static class MailPackage extends AbstractSendable {
            private final Package content;

            public MailPackage(String from, String to, Package content) {
                super(from, to);
                this.content = content;
            }

            public Package getContent() {
                return content;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                if (!super.equals(o)) return false;

                MailPackage that = (MailPackage) o;

                if (!content.equals(that.content)) return false;

                return true;
            }

        }

        /*
    �����, ������� ������ �������. � ������� ���� ��������� �������� ����������� � ������������� ��������.
    */
        public static class Package {
            private final String content;
            private final int price;

            public Package(String content, int price) {
                this.content = content;
                this.price = price;
            }

            public String getContent() {
                return content;
            }

            public int getPrice() {
                return price;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Package aPackage = (Package) o;

                if (price != aPackage.price) return false;
                if (!content.equals(aPackage.content)) return false;

                return true;
            }
        }


        /*
    ���������, ������� ������ �����, ������� ����� �����-���� ������� ���������� �������� ������.
    */
        public static interface MailService {
            Sendable processMail(Sendable mail);
        }

        /*
        �����, � ������� ������ ������ ��������� �����
        */
        public static class RealMailService implements MailService {

            @Override
            public Sendable processMail(Sendable mail) {
                // ����� ������ ��� ��������� ������� �������� �����.
                return mail;
            }
        }

        public static final String AUSTIN_POWERS = "Austin Powers";
        public static final String WEAPONS = "weapons";
        public static final String BANNED_SUBSTANCE = "banned substance";
        /*UntrustworthyMailWorker � �����, ������������ ����������� ��������� �����, ������� ������ ����,
        ����� �������� �������� ������ ��������������� � ������ �����, ��������������� �������� ���� ������
        ������ ������� ���, � �����, � ����� ������, �������� ������������ ������ ��������������� ����������
        RealMailService. � UntrustworthyMailWorker ������ ���� ����������� �� ������� MailService
        ( ��������� ������ processMail ������� �������� ������� ���������� �� ���� processMail ������� ��������, � �. �.)
         � ����� getRealMailService, ������� ���������� ������ �� ���������� ��������� RealMailService.
         */
        public static class UntrustworthyMailWorker implements MailService{
            public RealMailService R = new RealMailService();
            public MailService[] mas;
            public UntrustworthyMailWorker(MailService[] mas)
            {this.mas = new MailService[mas.length];
                for(int i = 0; i < mas.length; i++)
                {
                    this.mas[i] = mas[i];
                }
            }
            @Override
            public Sendable processMail(Sendable mail) {
                Sendable newmail = mail;

                for (int i = 0; i < this.mas.length; i++) {
                    newmail = mas[i].processMail(newmail);
                }
                return R.processMail(newmail);
                //return newmail;
            }
            public RealMailService getRealMailService(){
                return this.R;
            }

        }


        /* Spy � �����, ������� ��������� � ���� �������� ���������, ������� �������� ����� ��� ����. ������ ��������������
        �� ���������� Logger, � ������� �������� ����� ����� �������� � ���� ���������. �� ������ ������ �� ��������� ������
        MailMessage � ����� � ������ ��������� ��������� (� ���������� ����� �������� ����� � �������� ������� �� �������� ����� �����):
        2.1) ���� � �������� ����������� ��� ���������� ������ "Austin Powers", �� ����� �������� � ��� ��������� � ������� WARN:
        Detected target mail correspondence: from {from} to {to} "{message}"
        2.2) �����, ���������� �������� � ��� ��������� � ������� INFO: Usual correspondence: from {from} to {to}
         */
        public static class Spy implements MailService{
            public Logger Log;
            public Spy(Logger Log)
            {
                this.Log = Log;
            }
            @Override
            public Sendable processMail(Sendable mail) {
                try {
                    MailMessage newmail = (MailMessage) mail;
                    if (mail.getFrom() == AUSTIN_POWERS || mail.getTo() == AUSTIN_POWERS) {
                        this.Log.log(Level.WARNING, "Detected target mail correspondence: from {0} to {1} \"{2}\"", new Object[]
                                {mail.getFrom(), mail.getTo(), newmail.message});
                    } else {
                        this.Log.log(Level.INFO, "Usual correspondence: from {0} to {1}", new Object[]
                                {mail.getFrom(), mail.getTo()});
                    }
                    // ����� ������ ��� ��������� ������� �������� �����.
                }
                catch (Throwable e){}
                return mail;
            }

        }


    /*Thief � ���, ������� ������ ����� ������ ������� � ���������� ��� ���������. ��� ��������� � ������������ ����������
    int � ����������� ��������� �������, ������� �� ����� ��������. �����, � ������ ������ ������ �������������� �����
    getStolenValue, ������� ���������� ��������� ��������� ���� �������, ������� �� ��������. ��������� ���������� ���������
    �������: ������ �������, ������� ������ ����, �� ������ �����, ����� ��, ������ � ������� ��������� � ���������� �������
    "stones instead of {content}".*/

        public static class Thief implements MailService {
            public int cost;
            private int Sum;
            public Thief (int cost){
                this.cost = cost;
                // this.Sum = 0;
            }
            @Override
            public Sendable processMail(Sendable mail) {

                if (mail instanceof MailPackage)
                {
                    MailPackage pac = (MailPackage) mail;
                    if (pac.getContent().getPrice() >= this.cost){
                        String f = "stones instead of " + pac.getContent().getContent();
                        //String f = "\"stones instead of {content}\"" + pac.content;
                        Package fool = new Package(f ,0);
                        MailPackage newmail = new MailPackage(mail.getFrom(), mail.getTo(), fool);
                        this.Sum += pac.getContent().getPrice();
                        return newmail;
                    }

                }
                // ����� ������ ��� ��������� ������� �������� �����.
                return mail;
            }

            public int getStolenValue(){
                return this.Sum;
            }

        }

        /*Inspector � ���������, ������� ������ �� ������������ � ����������� ��������� � ���� ������� � ���� ����������,
        ���� ���� ���������� �������� �������. ���� �� ������� ����������� ������� � ����� �� ����������� ����������
        ("weapons" � "banned substance"), �� �� ������� IllegalPackageException. ���� �� ������� �������, ��������� ��
        ������ (�������� ����� "stones"), �� ������� ��������� � ���� StolenPackageException. ��� ���������� �� ������
        �������� �������������� � ���� ������������� ����������.
         */
        public static class IllegalPackageException extends RuntimeException
        {
            public IllegalPackageException(){}
        }
        public static class StolenPackageException extends RuntimeException
        {
            public StolenPackageException(){}
        }
        public static class Inspector implements MailService {
            @Override
            public Sendable processMail(Sendable mail) {
                MailPackage pac = null;
                try {
                    pac = (MailPackage) mail;


                    if (pac.getContent().getContent().contains(WEAPONS) || pac.getContent().getContent().contains(BANNED_SUBSTANCE))
                        throw new IllegalPackageException();
                    if (pac.getContent().getContent().contains("stones"))
                        throw new StolenPackageException();
                }
                catch (java.lang.ClassCastException e){}

                // ����� ������ ��� ��������� ������� �������� �����.
                return mail;
            }

        }

    }


