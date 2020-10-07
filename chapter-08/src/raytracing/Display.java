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

        List<Hitable> objList = new ArrayList<Hitable>();
        objList.add(new Sphere(new Vec3(0.0f, 0.0f, -1.0f), 0.5f, new Metal(new Vec3(0.8f, 0.6f, 0.2f), 0.1f)));
        objList.add(new Sphere(new Vec3(0.3f, 0.0f, -1.0f), 0.3f, new Metal(new Vec3(0.8f, 0.6f, 0.2f), 0.1f)));
        objList.add(new Sphere(new Vec3(0.0f, -100.5f, -1.0f), 100f, new Metal(new Vec3(0.8f, 0.8f, 0.2f), 0.1f)));
        Hitable world = new HitableList(objList);

        Camera camera = new Camera();
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
        }
    }

    public Vec3 randomInUnitSphere() {
        Vec3 p;
        do {
            p = new Vec3((float)(Math.random()), (float)(Math.random()), (float)(Math.random())).Scale(2.0f).Subtract(new Vec3(1.0f, 1.0f, 1.0f));
        } while (p.dot(p) >= 1.0f);
        return p;
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
