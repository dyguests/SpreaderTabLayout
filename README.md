# SpreaderTabLayout

[![](https://jitpack.io/v/dyguests/SpreaderTabLayout.svg)](https://jitpack.io/#dyguests/SpreaderTabLayout)

A TabLayout that each tab can be expanded,custom./一个Tab可以撑开、自定义的TabLayout。

![sample](./graphics/sample.gif)

**Use AndroidX Artifacts.**

**Use Kotlin.**

## Import

###### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

###### Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.dyguests:SpreaderTabLayout:x.x.x'
	}

## Usage

in xml:

        <com.fanhl.spreadertablayout.SpreaderTabLayout
            android:id="@+id/spreader_tab_layout"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/tab_layout">
    
            <include
                android:id="@+id/item_1"
                layout="@layout/item_1"/>
    
            <Button
                android:id="@+id/item_2"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:maxLines="1"
                android:text="SECOND"/>
    
            <Button
                android:id="@+id/item_3"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:maxLines="1"
                android:text="THIRD"/>
        </com.fanhl.spreadertablayout.SpreaderTabLayout>

in code:

    spreader_tab_layout.setupWithViewPager(view_pager)
    
    spreader_tab_layout.setupWithRecyclerView(recycler_view)

**setup with RecyclerView will auto add PagerSnapHelper.**

## Notice

### 1. The child of SpreaderTabLayout now support **Normal view**,**MotionLayout(ConstraintLayout 2.0)**.

#### 1.1. normal view.

    ...

#### 1.2. MotionLayout

**Cool interactive effects**

You should set constraintLayout's constraintSet use `app:layoutDescription`.

        <androidx.constraintlayout.motion.widget.MotionLayout
            ...
            app:layoutDescription="@xml/motion_item_1">
            ...
        </androidx.constraintlayout.motion.widget.MotionLayout>
    
You should learn `MotionLayout` first.

See:

[Introduction to MotionLayout (part I)](https://medium.com/google-developers/introduction-to-motionlayout-part-i-29208674b10d)

## TODO

- [x] The SpreaderTabLayout only supported match_parent now!
- [ ] support RecyclerView.
- [ ] tab background. 