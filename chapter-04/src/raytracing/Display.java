package raytracing;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Display {
    public int width;
    public int height;

    private Vec3 lower_left = new Vec3(-2.0f, -1.0f, -1.0f);
    private Vec3 horizontal = new Vec3(4.0f, 0.0f, 0.0f);
    private Vec3 vertical = new Vec3(0.0f, 2.0f, 0.0f);
    private Vec3 origin = new Vec3(0.0f, 0.0f, 0.0f);

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

        try {
            FileWriter fw = new FileWriter(pictureName);
            fw.write("P3\n" + width + " " + height + "\n255\n");

            for(int j = height - 1; j >= 0; j--) {
                for(int i = 0; i < width; i++) {
                    float u = (float)i/(float)width;
                    float v = (float)j/(float)height;
                    // 一条光线
                    Ray r = new Ray(origin, lower_left.Add(horizontal.Scale(u)).Add(vertical.Scale(v)));
                    Vec3 col = color(r);


                    float b = 0.2f;
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

    /**
     * 是否碰到球
     * @param center 球心
     * @param radius 半径
     * @param r 光线
     * @return 是否与球相交
     */
    public boolean hitSphere(final Vec3 center, float radius, final Ray r) {
        Vec3 oc = r.origin().Subtract(center);
        float a = r.direction().dot(r.direction());
        float b = 2.0f * oc.dot(r.direction());
        float c = oc.dot(oc) - radius * radius;
        float discriminant = b * b - 4 * a * c; // 二元一次方程根的判别式 b^2 - 4ac

        return !(discriminant < 0);
    }

    public Vec3 color(Ray r) {
        if (hitSphere(new Vec3(0, 0, -1), 0.5f, r)) {
            return new Vec3(0, 0, 1);
        } else {
            Vec3 unit_dir = r.direction().normalize(); // 单位方向向量
            float t = 0.5f * (unit_dir.y() + 1.0f); // 原本范围为[-1, 1]，调整为[0, 1]
            // 返回背景；沿y轴线性插值，返回的颜色介于白色和蓝色之间
            return new Vec3(1.0f, 1.0f , 1.0f).Scale(1.0f - t).Add(new Vec3(0.5f, 0.7f, 1.0f).Scale(t));
        }
    }
}
