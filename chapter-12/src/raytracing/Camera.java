package raytracing;

public class Camera {
    private Vec3 lower_left; // 画布左下角点
    private Vec3 horizontal; // 宽
    private Vec3 vertical; // 高
    private Vec3 origin; // 相机原点
    private float lens_radius;
    private Vec3 u = new Vec3();
    private Vec3 v = new Vec3();
    private Vec3 w = new Vec3();


    public Camera() {
        lower_left = new Vec3(-2.0f, -1.0f, -1.0f);
        horizontal = new Vec3(4.0f, 0.0f, 0.0f);
        vertical = new Vec3(0.0f, 2.0f, 0.0f);
        origin = new Vec3(0.0f, 0.0f, 0.0f);
    }


    /**
     *
     * @param lookfrom 相机位置
     * @param lookat 观察点
     * @param vup 相机的倾斜方向 view up
     * @param vfov 视野 field of view
     * @param aspect 宽高比
     */
    public Camera(Vec3 lookfrom, Vec3 lookat, Vec3 vup, float vfov, float aspect, float aperture, float focus_dist){
        lens_radius = aperture / 2;

        lens_radius = aperture / 2;

        float theta = (float)(vfov * Math.PI / 180);
        float half_height = (float)( Math.tan(theta/2) );
        float half_width = aspect * half_height;
        origin = lookfrom;
        w = lookfrom.Subtract(lookat).normalize();      //相当于新的z
        u = vup.cross(w).normalize();                   //相当于新的x
        v = w.cross(u).normalize();                     //相当于新的y
        lower_left = origin.Subtract(u.Scale(half_width*focus_dist)).Subtract(v.Scale(half_height*focus_dist)).Subtract(w.Scale(focus_dist));
        horizontal = u.Scale(2*half_width*focus_dist);
        vertical = v.Scale(2*half_height*focus_dist);
    }

    /**
     * @param u 距离lower_left的横向距离
     * @param v 距离lower_left的纵向距离
     * @return 光线向量
     */
    public Ray GetRay(float u, float v) {
        Vec3 rd = randomInUnitSphere().Scale(lens_radius);
        Vec3 offset = this.u.Scale(rd.x()).Add(this.v.Scale(rd.y()));
        return new Ray(origin.Add(offset), lower_left.Add(horizontal.Scale(u)).Add(vertical.Scale(v)).Subtract(origin).Subtract(offset));
    }

    /**
     * 生成单位球内的随机坐标
     * @return 单位球内的随机坐标
     */
    public Vec3 randomInUnitSphere() {
        Vec3 p;
        do {
            p = new Vec3((float)(Math.random()), (float)(Math.random()), (float)(Math.random())).Scale(2.0f).Subtract(new Vec3(1.0f, 1.0f, 1.0f));
        } while (p.dot(p) >= 1.0f);
        return p;
    }
}
