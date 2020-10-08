package raytracing;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Display {
    public int width;
    public int height;

    static String init(String title) {
        SimpleDateFormat df = new SimpleDateFormat("HH_mm_ss");
        String outputPath = "./";
        String pictureName = outputPath + title + df.format(new Date()) + ".ppm";
        return pictureName;
    }

    public Display() {
        this(200, 100, "ray_tracer");
    }

    public Display(int width, int height, String title) {
        this.width = width;
        this.height = height;

        String pictureName = Display.init(title);
        System.out.println("pictureName: " + pictureName);

        Hitable world = new HitableList(randomScene());

        float aspect = (float)width/(float)height;  //宽高比
        Vec3 lookfrom = new Vec3(3, 3, 2);
        Vec3 lookat = new Vec3(0, 0, -1);
        float dist_to_focus = (lookfrom.Subtract(lookat)).length();
        float aperture = 2.0f;
        Camera camera = new Camera(lookfrom, lookat, new Vec3(0, 1, 0), 20, aspect, aperture, dist_to_focus);

        int ns = 50; // 采样次数，消除锯齿

        try {
            FileWriter fw = new FileWriter(pictureName);
            fw.write("P3\n" + width + " " + height + "\n255\n");

            for(int j = height - 1; j >= 0; j--) {
                for(int i = 0; i < width; i++) {
                    Vec3 col = new Vec3(0, 0, 0);
                    for(int s = 0; s < ns; s++) {
                        float u = (float)(i + Math.random())/(float)width; // 添加随机数，消除锯齿
                        float v = (float)(j + Math.random())/(float)height;
                        // 一条光线
                        Ray r = camera.GetRay(u, v); // 根据uv得出光线向量
                        col = col.Add(color(r, world, 0)); // 根据每一个像素点(光线)上色，并累加
                    }
                    col = col.Scale(1.0f/(float)ns); // 除以采样次数，求平均值
                    col = new Vec3((float)Math.sqrt(col.x()), (float)Math.sqrt(col.y()), (float)Math.sqrt(col.z())); // gamma矫正

                    int ir = (int)(255.59f*col.x());
                    int ig = (int)(255.59f*col.y());
                    int ib = (int)(255.59f*col.z());
                    fw.write(ir + " " + ig + " " + ib + "\n");
                }
            }

            fw.close();

        } catch (Exception e) {
            System.out.println("display 异常");
            e.printStackTrace();
        }
    }

    List<Hitable> randomScene() {

        List<Hitable> objList = new ArrayList<Hitable>();
        //超大漫反射球作为地板
        objList.add(new Sphere(new Vec3(0.0f,-1000.0f,0.0f), 1000.0f, new Lambertian(new Vec3(0.5f, 0.5f, 0.5f))));
        //定义三大球
        objList.add(new Sphere(new Vec3(0, 1, 0), 1.0f, new Dielectric(1.5f)));
        objList.add(new Sphere(new Vec3(-4, 1, 0), 1.0f, new Lambertian(new Vec3(0.4f, 0.2f, 0.1f))));
        objList.add(new Sphere(new Vec3(4, 1, 0), 1.0f, new Metal(new Vec3(0.7f, 0.6f, 0.5f), 0.0f)));

        //生成地面小球
        int i = 1;
        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                /*两个for循环中会产生（11+11）*(11+11)=484个随机小球*/
                float choose_mat = (float)Math.random();
                /*产生一个（0，1）的随机数，作为设置小球的材质的阀值*/
                Vec3 center = new Vec3((float)( a+0.9*(Math.random()) ), 0.2f, (float) ( b+0.9*(Math.random() )));
                /*球心的x,z坐标散落在是（-11，11）之间的随机数*/
                if ((center.Subtract(new Vec3(4,0.2f,0))).length() > 0.9) {
                    /*避免小球的位置和最前面的大球的位置太靠近*/
                    if (choose_mat < 0.8) {     //diffuse
                        /*材料阀值小于0.8，则设置为漫反射球，漫反射球的衰减系数x,y,z都是（0，1）之间的随机数的平方*/
                        objList.add(
                                new Sphere(center, 0.2f, new Lambertian(
                                        new Vec3((float)( (Math.random())*(Math.random()) ),
                                                (float)( (Math.random())*(Math.random()) ),
                                                (float)( (Math.random())*(Math.random()) ))
                                ))
                        );
                    }
                    else if (choose_mat < 0.95) {
                        /*材料阀值大于等于0.8小于0.95，则设置为镜面反射球，镜面反射球的衰减系数x,y,z及模糊系数都是（0，1）之间的随机数加一再除以2*/
                        objList.add(
                                new Sphere(center, 0.2f, new Metal(
                                        new Vec3((float)( 0.5f*(1+(Math.random())) ), (float)( 0.5f*(1+(Math.random())) ), (float)( 0.5f*(1+(Math.random()))) ),
                                        (float)( 0.5*(1+(Math.random())))
                                ))
                        );
                    }
                    else {
                        /*材料阀值大于等于0.95，则设置为介质球*/
                        objList.add(
                                new Sphere(center, 0.2f, new Dielectric(1.5f))
                        );
                    }
                }
            }
        }
        return objList;
    }

    public Vec3 color(Ray r, Hitable world, int depth) {
        HitRecord rec = new HitRecord();
        // 是否有撞击点
        if (world.hit(r, 0.001f, Float.MAX_VALUE, rec)) {
            // 任何物理有撞击点
            Wrapper wrapper = new Wrapper();
            if (depth < 50 && rec.matPtr.scatter(r, rec, wrapper)) {
                return color(wrapper.scattered, world, depth + 1).Multiply(wrapper.attenuation);
            } else {
                return new Vec3(0, 0, 0);
            }
        } else {
            Vec3 unit_dir = r.direction().normalize(); // 单位方向向量
            float t = 0.5f * (unit_dir.y() + 1.0f); // 原本范围为[-1, 1]，调整为[0, 1]
            // 返回背景；沿y轴线性插值，返回的颜色介于白色和蓝色之间
            return new Vec3(1.0f, 1.0f , 1.0f).Scale(1.0f - t).Add(new Vec3(0.5f, 0.7f, 1.0f).Scale(t));
        }
    }
}
