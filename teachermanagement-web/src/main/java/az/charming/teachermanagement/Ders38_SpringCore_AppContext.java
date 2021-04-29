/*Spring Core Annotation - Proyektde yazdigimiz deyisenlere (StudentController clasindaki studentRepository,
studentService kimi) menimsedilen obyektler (aoutowired, injection vasitesile spring terefinden injeksiya olunan)
eslinde real obyektler deyil , real deyende yeni ki bu deyisene misal- qurmusug StudentService clasini ve
StudentControllerdeki deyisene cerez konstruktor obyekti otursun deyirik ve
private final StudentService studentService -e hecde bunun implementasiyasi oturmur yeni new StudentService() bele bir
sey oturmur. Meselen her hansisa bir metodu cagiranda studentService-in clas name-ni ekrana versek goreceyik ki  bele
bir obyekti qurulub - StudentService$Proxy yeni bele eslinde bayag dediyimiz obyekt bu sekilde oturur-
private final StudentService studentService = new StudentService$Proxy();
Bu nedir? ne ucun lazimdir - obyektin qurulmasi, obyektin injekt olunmasi,obyektin lifecycle-nin idare olunmasi spring
terefinden idare olunur - buna da Inversion of Control deyilir (IoC) (kontrolun qarsi terefe oturulmesi yeni bunu biz
etmirik, springe otururuk ki sen et). Tutaq ki biz isteyirik ki singleton pattern-li ve ya diger patternle her defe yeni
bir obyekt qurulsun, bunlarin hamisini springe oture bilersiz ve cagiranda da yene deyeceksen mene obyekt lazimdir o
sene hemin obyekti qurub verecek.Belelikle bu prosese IoC deyirik ve bu prosesi elde ede bilmek ucun 3 dene anlayisdan
istifade olunur- Aspect Oriented Programming (AOP), Dependecy Injection (DI) ve ProxyPattern.
Obyektin uzerinde managementi elde elemek isteyirse evvelce onu wrap etmelidir ki onu idare ede bilsin, direkt olaraq
bunu verse bir menasi olmur. Onu wrap eleyir ve ozu qerar verir ki bu obyektin uzerinde neler edilmelidir yaxud neler
etmek isteyir- bu Proxy Pattern-nin bize vermis oldugu imkandir.
Dpenedency Injection - bunu istifade edir ki obyekti istediyi vaxt istediyi deyisene injekt ede bilsin ve Dependency
Injection-i AOP vasitesile edir.
AOP- bu odurki proyekt daxilden hec bir mudaxile etmeden kenardan nese ede bilsin. Misal- hazirda clasimizda
(StudentControllerde) springlik bir sey yoxdu yeni direkt olarag muraciet elemir, yeni xususi bir sey yazmamisan ki
spring obyekti qursun bu deyisene set elesin. Ortada ele bir veziyyetdir ki yeni sen sadece bildiyin claslarla isivi
gorursen spring kenardan AOP vasitesile xeberin bele olmadan obyektive mudaxile edir, obyektleri set edir, yaxudda hemen
obyektlerin lifecycle-larini idare edir. Ya singleton edir yada qeyri singleton- buna springde prototype deyirler.
Default (deyendeki mene obyekt ver) olarag obyektler singleton ile qurulur istesek bunu prototype da ede bilirik - bu
interview sualidir yeni default olarag nece qurulur?
Springde hemcinin bele bir sey var proyekt ayaga qalxdigi zaman butun obyketlerden (yeni acilan claslarin- controllerin,
servicelerin, repositorylerin) en azi bir denesini qurur. Qisasi bele deyek uzerinde @Component olan ne varsa hamisindan
qurur. Lazim olanda onlari injekt ede bilsin, istifade ede bilsin.Lakin istesek bunu prototype da ede bilerik yeniki her
defe yeni bir obyekt qura bilsin. Proyekt ayaga qalxanda spring qurmali oldugu claslar haqdaki informasiyani meta datada
saxlayir ve bu meta dataya gore hereket eleyir hansini prototype elan edib, hansini singleton, hansi service clasidir,
hansi controllerdi ve s. melumatlari saxlayir.
Spring Context anlayisi- bize her hansisa bir obyekt verilirse o spring contextden goturulur. Context deyende konteyner
agila gelir ele bir nov onun kimi dusunmek olar, yeni obyektlerin size dartilmasi, qaytarilmasi ucun istifade olunur bu
context. Stackover- de context get bean yazib bir misala baxag-
@Component
public class SpringContext implements ApplicationContextAware{
     private static ApplicationContext applicationContext;
bele bir clas acilib appilcationContext deyisenine AppicationContext obyekti injekt olur. Asagida getBean metodu
cagirilir- applicationContext.getBean(String serviceName,Interface.Class) bu metodla deyirik ki filan tipde (Interface)
filan adli (serviceName) beani ver. Oda bize hemen obyekti qaytarir. Spring birbasa bizim obyekte  injekt elemeseydi bu
cur goturerdik- yeni bu misala ona gore baxdiq.
Configuration anlayisi - bir dene proyektde yeni teachermanagement paketinde configuration paketi icinde de
UrlConfiguration clasi acirig -
@Configuration
public class UrlConfiguration {
    @Value("${students.url}")
    private String studentsUrl;

    public String getStudentsUrl() {
        return studentsUrl;
    }
}
deyisenin basina url-i yazirig ve application properties-de oz urlimizi deyer kimi menimsedirik -
students.url=http://localhost:8080/rest/students bu url-e deyeri veririk ve burdaki deyeri @Value default olaraq
appilaction propertiesden goturur clasdaki studentsUrl deyisenine set edir. Clasin uzerine @Component yazsaqda isleyer
sadece bunun @Configurationdan ferqi odurki @Component-de sen hemen clasin istifade etidyin yerde adini deyisenini yazib
@autowired etmelisen ki o component qurulsun, set olunsun ve hemin clasin icindeki sen neyise istifade edesen.
Configuration ise hec neyi gozlemir cunki bir seyin ise duse bilmesi ucun konfiqurasiya olunmali, ayarlari set
olunmalidir. Ona gore adı configuration qoyulub ele, hec neyi gozlemeden proyekt ayaga qalxdisa initialize olunacaq,
icindeki butun deyerler set olunacaq ve gozleyecek ki  icindeki obyektin adini cekib istifade edesen, @autowired ehtiyac
qalmir. Gelirik StudentService tutaq ki hansisa url goturub ora sorgu gondereceyik. 2 dene deyisen yazirig -
private final UrlConfiguration urlConfiguration;
private final RestTemplate restTemplate;
Bunlari konstruktora elave edirik. Resttemplate- adi restful apiler request gondermeden gelir ancag yalniz restful
apiler ucun deyil, request gondermek ucun bir clasdir, obyekti qurulur ve biz istifade edirik, bununcun bele bir metod
yazirig-
public void getStudents(){
    ResponseEntity<StudentResponseDto[]> studentsResponseEntity =
             restTemplate.getForEntity(urlConfiguration.getStudentsUrl(), StudentResponseDto[].class);
    StudentResponseDto[] students = studentsResponseEntity.getBody();
    for(StudentResponseDto studentResponseDto: students){
          System.out.println(studentResponseDto);
    }
}
bu metodda restTemplate-in getForEntity metoduna url-i otururuk hemcinin urlden ne cavab qayidacagsa hemin cavabin
clasini (StudentResponseDto[].class) otururuk. Bu clasdan bize ResponseEntity qayidir deye deyisene menimsedirik.
Asagida sonra hemin deyisenle studentsResponseEntity getbody metodunu cagiririq oda bize ele bunu verir
StudentResponseDto[] onuncun da menimsedirik ve casting-e ehtiyac olmasin deye yuxarida ResponseEntity-e generik type-ni
verirsen - ResponseEntity<StudentResponseDto[]> ve gormek ucun capa verirsen fora qoyaraq.
Konstruktor restTemplate xeta verir (could not autowired deyir) yeni deyir bele bir obyekt yazmisan autowire ele, ancaq
hardan auto wire edim deyir. Buna gore bir dene de Configuration clasi duzeldirik ve bununla biz configuration clasinin
mahiyyetini ve bean anlayisini oyrenmis olacayiq-
@Configuration
public class AppConfiguration {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
bele bir clas acdig configuration clasinda, umumi AppConfiguration dedik adina cunki her sey ucun ayrica configuration
acmirlarda, bunun icinde artiq sene ne lazimdirsa yazirsan. Burda metod RestTemplate adli obyekt qurur ve geriye
qaytarir.Clasin basinda @Configuration oldugu ucun hec kesi gozlemir avtomatik ise dusur ve iceride spring terefinden
metod avtomatik cagirilir ve metoddan qaytarilan obyekt @Bean yazildigi ucun yadda saxlanilir ve bu adda - RestTemplate
saxlanilir.Belelikle xeta aradan qalxdi.
StudetnService deki restTemplate-in adina gore baglanti qurulmur yeni burdaki deyisenlerin adini deyissekde ,
AppConfi-deki metodun da adini ferqli yazsag problem olmur cunki tipe gore hereket edir.RestTemplate-de Spring
framworkden gelir lazim olanda bele qurub, yadda saxlayib istifade ede bilirik. Sual ola biler ki niye StudentRepository
ucun, SutdentService ucun bunlari etmedik? RestTemplate adi bir clasdir baxsaq gorerik uzerinde hec ne yoxdu, ancag biz
isteyende ki spring terefinden bu clasin obyekti qurulsun, istenilen vaxt oturulsun, injekt olunsun bunu etmek ucun mehz
bunun bean-ni qurursan. Bu clasin @componenti filan hec neyi yoxdu ona gore spring axtarmir bunu meselen
StudentService-i filan niye idaresi altina alir cunki @Service var uzerinde. Belelikle adi claslari isteyendeki nece
edek onu spring idaresi altina alsin bu zaman @bean-den istifade edirik, obyektini qurub bean olaraq return edirik. Bu
qeder artiq springin daxilinde bele bir obyekt var ve o injekt oluna biler.
Basqa bir numuneye baxaq tutalim her hansi paketde bir clas acdiq gostermek ucun ancaq ferz edin proyektde yerlesmir bu
clas basqa bir kitabxanadan goturmusuk ve adini qoydug meselen CoxLazimliClas. Icinde bir metod acdig meselen public
void coxLazimliMetod() adli. Bu basqa bir kitabxanadan geldiyi ucun, oz proyektimizde olmadigi ucun uzerine @Component
yaza bilmirik ancaq isteyirik ki spring bunu oz ehatesi altina alsin. Bununcun yene @Bean-den istifade edirik. Bayag
acdigimiz AppConfiguration clasinda bu clas tipli metod acib obyektini qurub return edirik ve uzerine @Bean yazarag-
@Bean
public CoxLazimliClas getCoxLazimliClas()
       return new CoxLazimliClas();
}
sonra da tutaq ki bayaq ki tek StudentServicede istifade edirik - deyisenini elan edib konstruktora elave etmekle. Bu
@Bean anlayisini evvel kecdiyimiz pojo, bean ile qarisdirmayin. Bu sadece springde bir annotasiyadir.
@Configuration anlayisini bir daha izah edek- eger @component olsa AppConfiguration clasin uzerinde o zaman icindekini
hardasa istifade edende gerek orda AppConfiguration-nin adini cekek ve uzerine @Autowired yazaq, ancaq @Configuration
olanda clasin adini cekmeye ve @Autowired yazmaga ehtiyac olmur birbasa icindekini istifade ede bilirsen, yeni deyir
lazim deyil meni cagirmaq men konfiqurasiyayam proyekt ise dusen kimi men ise dusmeliyem. Neceki evvelki derslerde
uzerinde @Service olan claslari hardasa cagiranda elan edirdik, cunki icine baxsaq onunda uzerinde @Component var.
Bir seyde elave edek- biz UrlConfiguration-i StudentService-de cagirdig (deyisenini yazib elan ederek). Mesele
burasindadir ki @Configuration yazilib deye onu cagirmamalisan, cagirmaq olmaz deye bir sey yoxdur. Bu configurationdir,
proyekt ayag qalxan kimi url-ni o deqiqe goturur studentsUrl-e menimsedir, hazir gozleyir okey amma istesen bunu
autowire edib istifade de ede bilersen. Bu url oldugu ucun ve bunun get metodunu cagirmag ehtiyacimiz oldugu ucun biz
bunu (UrlConfiguration-i) autowire edirik ( private final UrlConfiguration urlConfiguration;) StudentService-de. Bele
deyek UrlConfiguration-a @Component-de yaza bilerik bu da menalidir cunki autowire edirik ancaq bu, confiqurasiya
rolunda istirak edir yeniki deyir mende proyektin islemesi ucun lazim olan bezi materiallar var, sene lazim olsa burdan
goturersen. Amma AppConfiguration konkret konfiqurasiyadir onu autowire etmeye ehtiyac yoxdur, beanleri qurur ve lazim
olandan olana onu istifade edirsen.
Appilcation Context-e baxacayiq xml uzerinden (lazim deyil elede ancaq gorun). Spring ilk cixanda xml ile isleyirdi
sonradan annotasiyalar ise dusdu ve annotasiyalarla basladi islemeye , xml tergidildi. Xml-lerde zulmdur, evvel filan
adda obyekt qururam deyende xml-lerle defination verilirdi, onunla gosterilirdi. Sonradan annotasiyalar ise dusdur,
bununla baslandi istifade edilmeye.
HATEOAS- google da yazib bir ence numunelere baxirig, meselen bir restful api yazilib lakin xml ile, xml ile olanda da
restful api sayilir, casmayin. Basqa bir numunede  account verilib, sonra accountun nomresi, accountun balansi, balansin
icinde currency, value bele bir struktur qurublar ve burada links deye bir hisse var , deposit, withdraw, transfer,
close heresi ucun bir url  yazib. Yeni sene kecidler verib ki bu kecidler vasitesile linkleri cagira bilesen. Meselen
bunun students ucunu ola bilerdi links melumatlarinda yazardiki teachers of student - bu studentin teacherlerini
goturmek ucun, /students/1/teachers bele bir linkde vere bilerdi. Bir sozle obsi datani senin qarsina qoymaqla cerez
cerez linkler verirki bu linklerle kece bilesen.Bu da HATEOAS sturukturudur. Ancag menasizdir deye istifade olunmur
cunki isleyen fikirlesir ki bu data lazimdirsa qoy qabagima istesem istifade ederem, day ora bura gonderme meni.
Spring Core xml - spring core xml configuration yazib google-da baeldung saytinda numuneye baxirig. Burada dependency
elave olunub ancag spring-context  yazilib, burada spring bootdan sohbet getmir sirf springin konteksti elave olunub ki
springin konteyner anlayisi, obyektlerin qurulma anlayisi, injekt olunma anlayisi istifade olunsun. Cunki springin
icinde ancaq injection deyil axi springin diger kitabxanalari da var melesen spring data deyirik, boot , web deyirik ve
s. Sonra dusuruk asagi bize lazim olan bu hissedi-
<bean
  id="indexService"
  class="com.baeldung.di.spring.IndexService" />

<bean
  id="indexApp"
  class="com.baeldung.di.spring.IndexApp" >
    <property name="service" ref="indexService" />
</bean>
Demek burada bean-i annotasiya ile yox xml ile yazilir ve ona ad verilir id ile ve ele bilki bizim metodumuz adidir yeni
@bean ile fikirlessek getIndexService-dir . Class verilir ki o bu klasin- IndexService obyekti olacaq. Sonra asagida ise
yuxarida qurmus oldugu service-i bunun icine - class="com.baeldung.di.spring.IndexApp oturur. IndexApp ele bir klasdirki
bu klasin konstruktoru IndexService tipinde obyekt teleb edir. Yeni asagida deyir IndexApp clasinin bean-ni qururam ve
yuxarda qurdugum obyekti burda IndexAppin konstruktoruna otururem property vasitesile ile ref-e baxsaq gorerik.
Detalli bilmeye ehtiyac yoxdur cunki artig bununla yazilmir sadece basa dusmek lazimdir ki kohneden xml ile yazanlar
isteye bilerki bunu oxuyun klas versiyasina kecirin.Indi her yerde spring boot istifade olunur.
Primary anlayisi- tutalim function paketinde StudentService copy edrik ve adina 2 yazirig. Bir denede
StudentServiceInter adli interface acirig ve her iki StudentService bunu implements edir.Bunu override elemek isteyirik-
StudentEndpointde olan studentService deyisenin tipini StudentServiceInter edirik anaq StudentEndpointdeki finadall,
findById falan metodlari error verir sebebi StudentServiceInter  ici bosdur ona gore StudentServiceInter2 clasindan
claslarin yalniz adlarini copy edib atirig-
public interface StudentServiceInter {
    public void getStudents();
    public void doSomething(StudentDto studentDto);
    public StudentDto findById(Integer id);
    public List<StudentDto> findAll(String name, String surname, Integer age,
                                    BigDecimal scholarship);
    public List<StudentEntity> findByAgeAndName(String name, Integer age);
    public StudentDto deleteById(Integer id);
    public void save(StudentDto studentDto);
    public void update(StudentDto studentDto);
}
StudentService clasinda getStudents metodu qurmusdug kecen dersde. Indi StudentEndpointde bu metodu cagirag .Onuncun
StudentEndpointde bir metod qururug adini da testStudents qoyurug -
@GetMapping(value ="/teststudents")
public void testStudents(){
       studentService.getStudents();
} - Bu metod neyleyir? demeli goturecek request atacag, requesti atacag yuxarda clasin basinda yazilan @RequestMapping
("/rest/students") - rest-e, sonra request gelib dusecek index metoduna, bu metodda  cavab qaytaracaq ve oda goturecek
istifade edecek.Run edirik proyekt ayaga qalxir ancaq biz bilmedik StudentService mi? yoxsa StudentService2 -mi autowire
etdi? Gelirik postmanda insert-e url-de testStudents elave edirik - http://localhost:8080/rest/students/teststudents ve
post olan sorgunu deyisib get edirik (cunki post metodu yoxdu get var). Bu api gelib testStudentsi cagiracag oda iceride
olan getStudents-i cagiracag. getStudents-de gedecek StudentService-de getStudents metodundaki restTemplate-den oz
apisini cagiracag -yeni StudentEndpointdeki index metodunu cagiracag (restTemplate-de url-e sorgu gonderir o sorgunu da
StudentResponseDto massivine cevirir, ele index metodunun da tipine baxanda goruruk bunu). Cunki application.properties-
de students.url-e rest/students yazmisig. Ele bilki application ozu ozune sorgu gonderir, neticeni alir ve o neticeni
cevirir obyekte.
Postmanda send edirik 200 ok yeni bos bir sey qaytarir.  burda test etmemizin meqsedi- men ele bil endpointi cagiriram,
endpoint studentService-i (yuxarda elan edilen deyisen) autowire edir, bu deyisende Inter interface-nin obyektinin
metodunu cagirir ve bura (private StudentServiceInter studentService;) injekt eleyib neyi? bayag dediyimiz kimi bilmek
isteyirik StudentService-imi yoxsa StudentService2 -nimi? Normalda error vermeliydi, demeliydiki 2 dene obyekt var men
bilmirem hansini autowire edim. Ancaq error vermemenin sebebi cox guman buna goredir- frameworklerde bez konvensiyalar
(razilasmalar) var, burda da bir hecnesiz -StudentService, birde StudentService2 bu var. Cox guman bele siralama var
deye komfilikt vermeyib birinci qabagina cixani goturub autowire edib.Debug lada baxsaq gorerik ki kod StudentService -e
gelir.
Burda problem olmadi deye primary-nin ne oldugunu gostere bilmedik. Problemin olmamasinin deqiq sebebi
StudentServiceInter-e baxir Inter interface kimi basa dusur bu anlasmadir ve interden solda yazilani yeni
StudentService-i goturur. StudentService1 edirik ve deyisen qirmizi verir yeni istediyimiz problem oldu. Indi bunu nece
aradan qaldirirlar? birinci variant deyisenin uzerinde @qualifier yazirsan ve hansi obyekti autowire edeceyini
bildirirsen-
@Autowired
@Qualifier("studentService1")
private StudentServiceInter studentService;
burda qualifiere oturduyumuz obyektin adini niye balaca yazdig? cunki proyekt ayaga qalxanda her bir clasin balaca ile
baslayan camel case obyektini qurur ve adini o cure verir. Bayag xml-de de gorduk  hemcinin clasin adi nedirsen onun
balaca yazilisi ile id verir ve sende hemen calsin id-sini yazarag cagira bilirsen.
Ikinci variantda ise @qualifier filan yazmirsan. Gedirsen clasin uzerinde (StudentService-de) @Primary yazirsan yeni ki
sen primary-sen konfilikt olarsa seni esas gotureceyik.
testStudents metodumuzun tipi void oldugu ucun sadece status yeni ok qayitdi, postmanda soz ve s. capa verilmedi.
@Transactional- relational strukturlu bazalarda tranzaksiya anlayisi olur. Meselen bir dene muellim daxil edirsen ve bu
muellime aid olan telebeleri daxil edirsen. Eger telebelerden her hansi biri insert olmasa bu muellimin insert olmasi
sene lazim deyil, bele bir hal qabaginiza cixib. Bele bir hallarda tranzaksiya acilir ve bu tranzaksiyanin icinde
muellim insert olunur, sonra muellime aid olan telebeler insert olunur. Ele ki hami insert olundu ondan sonra commit
buraxilir.Yeni ki tranzaksiya tesdiqlenir. Eger uygun olmasa rollback buraxilir.Bu anlayis jdbc'de de, jpa'da da, yeni
her yerde var.
jdbc'de baxaq misalcun- jdbc commit transaction yazib stackoverflow'da bele bir misala baxaq:
try
{
  con.setAutoCommit(false);

   //1 or more queries or updates

   con.commit();
}
catch(Exception e)
{
   con.rollback();
}
finally
{
   con.close();
}
Biz jdbc'de connection acirdig ancaq commit filan hec ne buraxmirdiq, cunki Autocommite false demirdik,  true idi deye
avtomatik olaraq commit olurdu.
Burda ise false verilib ona gore sonra commit vermek zeruriyyeti olur - con.commit();. Bu da o demekdir ki prosesi icra
edirik axirda commit buraxiriq. Yeni buralarda (//1 or more queries or updates) delete, update, insert ve s.
buraxmagimiz o demek deyil ki onlar icra olunur. Onlar sadece emeliyyat olaraq qeyde alinir ve commiti buraxanda
tesdiqlenir ve proses kecmis sayilir. Eger hemin emeliyyatlardan (update, delete ve s.) hansindasa problem olarsa seni
atir catch-e ve sen rollback edirsen. Yeni bayaqdan qeyde almis oldugu emeliyyatlarin hamisini imtina edirsen
(rollback). Sonda da close yeni commit olsada olmasada, rollback olsa da olmasa da connection-i axirda close edirem. Bu
commit, rollback, tranzaksiya anlayisidir. Bu anlayisi spring'de etmek ucunse- ya StudentService1 clasin uzerinde yada
StudentRepository-de @Transactional (spring frameworkden geleni) yazirsan. Bunu yazanda clasda olan emeliyyatlar ucun
tranzaksiya acilir, icra olunur, daha sonra tranzaksiya commit olunur.
Traznaksiya select emeliyyatlari ucun deyil(meselen find filan), delete, update, insert emeliyyarlari ucun olur yeni ki
bazada nese deyisende. Clasin uzerinde bunu yazanda tutalim delete edende bir necesini, metod ugurlu olub ve metoddan
cixanda goturur senincun tranzaksiyani commit edir. Bunu clasin uzerinede , metodun uzerinede yaza bilirsen.
Birde traditional jpa var.Buna baxmamisig ancaq narahat olmaga deymez, hec ne qeyri adi deyil cunki tutalim .deleteById
metodunda studentRepository.deleteById(id); yazmisan digerlerinde ise solda studentRepository yazmayacaqsan, tutaq ki
entityManager yazassan, yaxud basqa bir sey yazassan.Yeni ki qayda eynidir metodu cagiraraq datani almaq isteyirsen.Koda
baxan kimi tez basa dusessen. Onsuz muellim entitymanageri nezeri de olsa gosterib, yungulvari find'i, delete'i, filan
nece edirsen.
Application context'in autowire olunmasi, istifade olunmasi- spring core application conxtext yazib bealdung saytindan
bir maraqli koda baxaq:
ApplicationContext context = new ClassPathXmlApplicationContext("applicationcontext/user-bean-config.xml");
UserService userService = context.getBean(UserService.class);
assertNotNull(userService);
Demeli evveller bu beanleri alinmasi (application context) qurulanda bu cure kod yazilirdi. Deyilirdi ki bu xml'i gotur
oxu ve bir dene context qur yeni xml'i oxuyursan basa dusursen ki sende hansi nov klaslar, obyektler olacag ve sende
goturub bir dene application context qurursan. context.getBean(UserService.class) - deyende o oxudugu xml'in icine baxir
ki UserService tipinde definition elan edilme var yoxsa yox. Varsa goturur hemin clasin obyektini qurub sene qaytarir.Bu
kohne variantdir. Xml teqdim olunur, xml'de claslar yazilir, sonra o context'de deyirsen ki bu obyekti ver, bele. Buna
sadece anlayis olsun deye baxin. Bele bir kod yazmayacagiq.
Application contexi proyektde birbasa autowire edilmesine baxaq (belke ehtiyac olar hacansa). Indiki halda autowire
annotasiyasi ile yeni cerez cerez cagiririq, esline qalsa cagirmiriq. Meselen StudentService1-de studentRepository-ni
autowire etmisik cerez konstruktor, yeni dolayi yolla application context'de demisik ki bize bu obyekti ver. Indi ise
direkt olaraq edirik, controllerde edek ki mentiqli olsun, StudentEndpointde yazirig-
@Autowired
private ApplicationContext applicationContext;
tutalim StudentServiceInter  studentService deyisenine ehtiyacimiz var ve bunu autowirwe etmirik (@Autowired silirik),
hazirda havadadi, hec kes buna hecne menimsetmeyib, konstruktor da vermemisik. Ozumuz bele yazirig bu zaman , bir menasi
yoxdur sadece isin kokunu bilek deye yazirig:
public StudentEndpoint(){
     studentService = (StudentServiceInter)
                                       applicationContext.getBean("studentService1");
}
getBean duzeldib StudentServiceInter'e menimsedirik. Ele bilki Endpoint clasin quran kimi konsturuktorda
applicationContext-i goturursen deyirsen mene studentService1 ver ve menimsedirsen bura- studentService. Bunun basina
autowire yazanda yene bele olanda :
@Autowired
private StudentServiceInter studentService;
Spring bu yazadigimiz isi ozu edir.Kohnede xml verirdin deyirdin applicationContext duzelt ver mene xml'e esasen. Burda
ise ApplicationContext clasin basinda yazdigin annotasiyalara esasen duzelir. Harada component varsa onlarin beanlerin
qurur, harada konfiqurasiya varsa onlari avtomatik qurur, konfiqurasiya icindeki beanleri qurur ve bunlarin hamisini
doldurur context-e daha sonra istesen contexti ozun cagirarsan istesen birbasa autowire ile cagirarsan.
 */