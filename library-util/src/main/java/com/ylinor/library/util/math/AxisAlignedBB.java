package com.ylinor.library.util.math;

import org.joml.Vector3f;


public class AxisAlignedBB {
    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;

    public AxisAlignedBB(float x1, float y1, float z1, float x2, float y2, float z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public AxisAlignedBB(BlockPos pos) {
        this(pos.x(), pos.y(), pos.z(), (pos.x() + 1), (pos.y() + 1), (pos.z() + 1));
    }

    public AxisAlignedBB(BlockPos pos1, BlockPos pos2) {
        this(pos1.x(), pos1.y(), pos1.z(), pos2.x(), pos2.y(), pos2.z());
    }

    public AxisAlignedBB(Vector3f min, Vector3f max) {
        this(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public AxisAlignedBB setMaxY(float y2) {
        return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, y2, this.maxZ);
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        else if (!(p_equals_1_ instanceof AxisAlignedBB)) {
            return false;
        }
        else {
            AxisAlignedBB axisalignedbb = (AxisAlignedBB) p_equals_1_;
            return Double.compare(axisalignedbb.minX, this.minX) != 0 ? false : (Double.compare(axisalignedbb.minY, this.minY) != 0 ? false : (Double.compare(axisalignedbb.minZ, this.minZ) != 0 ? false : (Double.compare(axisalignedbb.maxX, this.maxX) != 0 ? false : (Double.compare(axisalignedbb.maxY, this.maxY) != 0 ? false : Double.compare(axisalignedbb.maxZ, this.maxZ) == 0))));
        }
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.minX);
        int j = (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minY);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minZ);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxX);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxY);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxZ);
        j = 31 * j + (int) (i ^ i >>> 32);
        return j;
    }

    public AxisAlignedBB addCoord(float x, float y, float z) {
        float d0 = this.minX;
        float d1 = this.minY;
        float d2 = this.minZ;
        float d3 = this.maxX;
        float d4 = this.maxY;
        float d5 = this.maxZ;
        if (x < 0.0D) {
            d0 += x;
        }
        else if (x > 0.0D) {
            d3 += x;
        }

        if (y < 0.0D) {
            d1 += y;
        }
        else if (y > 0.0D) {
            d4 += y;
        }

        if (z < 0.0D) {
            d2 += z;
        }
        else if (z > 0.0D) {
            d5 += z;
        }

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB expand(float x, float y, float z) {
        float d0 = this.minX - x;
        float d1 = this.minY - y;
        float d2 = this.minZ - z;
        float d3 = this.maxX + x;
        float d4 = this.maxY + y;
        float d5 = this.maxZ + z;
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB expandXyz(float value) {
        return this.expand(value, value, value);
    }

    public AxisAlignedBB union(AxisAlignedBB other) {
        float d0 = Math.min(this.minX, other.minX);
        float d1 = Math.min(this.minY, other.minY);
        float d2 = Math.min(this.minZ, other.minZ);
        float d3 = Math.max(this.maxX, other.maxX);
        float d4 = Math.max(this.maxY, other.maxY);
        float d5 = Math.max(this.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public void set(AxisAlignedBB other) {
        this.minX = other.minX;
        this.minY = other.minY;
        this.minZ = other.minZ;
        this.maxX = other.maxX;
        this.maxY = other.maxY;
        this.maxZ = other.maxZ;
    }

    public void set(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public AxisAlignedBB offsetLocal(float x, float y, float z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    public AxisAlignedBB offsetLocal(Vector3f offset) {
        this.minX += offset.x;
        this.minY += offset.y;
        this.minZ += offset.z;
        this.maxX += offset.x;
        this.maxY += offset.y;
        this.maxZ += offset.z;
        return this;
    }

    public AxisAlignedBB offsetLocal(BlockPos pos) {
        return this.offsetLocal(pos.x(), pos.y(), pos.z());
    }

    public AxisAlignedBB copy() {
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public AxisAlignedBB offset(float x, float y, float z) {
        return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public AxisAlignedBB offset(BlockPos pos) {
        return new AxisAlignedBB(this.minX + pos.x(), this.minY + pos.y(), this.minZ + pos.z(), this.maxX + pos.x(), this.maxY + pos.y(), this.maxZ + pos.z());
    }

    public float calculateXOffset(AxisAlignedBB other, float offsetX) {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (offsetX > 0.0D && other.maxX <= this.minX) {
                float d1 = this.minX - other.maxX;
                if (d1 < offsetX) {
                    offsetX = d1;
                }
            }
            else if (offsetX < 0.0D && other.minX >= this.maxX) {
                float d0 = this.maxX - other.minX;
                if (d0 > offsetX) {
                    offsetX = d0;
                }
            }

            return offsetX;
        }
        else {
            return offsetX;
        }
    }

    public float calculateYOffset(AxisAlignedBB other, float offsetY) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (offsetY > 0.0D && other.maxY <= this.minY) {
                float d1 = this.minY - other.maxY;
                if (d1 < offsetY) {
                    offsetY = d1;
                }
            }
            else if (offsetY < 0.0D && other.minY >= this.maxY) {
                float d0 = this.maxY - other.minY;
                if (d0 > offsetY) {
                    offsetY = d0;
                }
            }

            return offsetY;
        }
        else {
            return offsetY;
        }
    }

    public float calculateZOffset(AxisAlignedBB other, float offsetZ) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
            if (offsetZ > 0.0D && other.maxZ <= this.minZ) {
                float d1 = this.minZ - other.maxZ;
                if (d1 < offsetZ) {
                    offsetZ = d1;
                }
            }
            else if (offsetZ < 0.0D && other.minZ >= this.maxZ) {
                float d0 = this.maxZ - other.minZ;
                if (d0 > offsetZ) {
                    offsetZ = d0;
                }
            }

            return offsetZ;
        }
        else {
            return offsetZ;
        }
    }

    public boolean intersectsWith(AxisAlignedBB other) {
        return this.intersects(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    public boolean intersects(float x1, float y1, float z1, float x2, float y2, float z2) {
        return this.minX < x2 && this.maxX > x1 && this.minY < y2 && this.maxY > y1 && this.minZ < z2 && this.maxZ > z1;
    }

    public boolean func_189973_a(Vector3f p_189973_1_, Vector3f p_189973_2_) {
        return this.intersects(Math.min(p_189973_1_.x, p_189973_2_.x), Math.min(p_189973_1_.y, p_189973_2_.y), Math.min(p_189973_1_.z, p_189973_2_.z), Math.max(p_189973_1_.x, p_189973_2_.x), Math.max(p_189973_1_.y, p_189973_2_.y), Math.max(p_189973_1_.z, p_189973_2_.z));
    }

    public boolean isVecInside(Vector3f vec) {
        return vec.x > this.minX && vec.x < this.maxX ? (vec.y > this.minY && vec.y < this.maxY ? vec.z > this.minZ && vec.z < this.maxZ : false) : false;
    }

    public float getAverageEdgeLength() {
        float d0 = this.maxX - this.minX;
        float d1 = this.maxY - this.minY;
        float d2 = this.maxZ - this.minZ;
        return (d0 + d1 + d2) / 3.0f;
    }

    public AxisAlignedBB contract(float value) {
        return this.expandXyz(-value);
    }

    public boolean intersectsWithYZ(Vector3f vec) {
        return vec.y >= this.minY && vec.y <= this.maxY && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    public boolean intersectsWithXZ(Vector3f vec) {
        return vec.x >= this.minX && vec.x <= this.maxX && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    public boolean intersectsWithXY(Vector3f vec) {
        return vec.x >= this.minX && vec.x <= this.maxX && vec.y >= this.minY && vec.y <= this.maxY;
    }

    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public boolean hasNaN() {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }
}
