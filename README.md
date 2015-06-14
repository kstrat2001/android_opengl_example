# OpenGL coding challenge!

You'll build a full-screen app with two animated views that simply host pixel shaders, directly rendering every pixel on the screen. These views involve rendering several spheres.

Note: Much of this document refers to iOS/Apple libraries because the [original challenge](https://github.com/kstrat2001/opengl_example) was originally only on iOS.

## Part 1:

Build a full-screen OpenGL ES view using OpenGL ES 2.0 or 3.0, your choice. In the interest of time you may use platform-level conveniences like Apple's GLKView to build the view, but you'll get bonus points if you build it yourself using OpenGL ES API calls directly. Further bonus points are available if you make the view dynamically determine the availability of OpenGL ES and use 3.0 if present, 2.0 otherwise. The view should not rotate with the device. It can be limited to the phone form factor for simplicity.
Your view should just render a rectangle across the entire screen. The vertex shader will be very simple. Most of your time will be spent on the fragment shader. This should be in GLSL 1.1 or 3.0, your choice (1.1 is required to support OpenGL ES 2.0). You may use any references or sources you like, but provide the full text of the GLSL program. Do not use conveniences like those in GLKit to produce these effects.

- 1. Render a red circle at the center of the screen. Take up most of the screen width. Make the rest of the screen gray (#666666, or 0.4, 0.4, 0.4).
- 2. Add diffuse lighting to give a 3-D appearance. Assume the light shines normal to the viewport (in the z direction) and is perfectly uniform.
- 3. Add a seamless, repeating texture. Use any texture you like, but here are some public domain textures that can be rendered against any base color:
- 4. Add specular lighting. Make the sphere appear to be shiny.
- 5. Make the sphere appear to rotate around the y axis. That is, animate the texture and preserve the illusion of roundness.
- 6. Add some ambient lighting to prevent the edges of the sphere from fading entirely to black against the gray background.

## Part 2:

Add a second full-screen pixel shader view like the one above (probably an instance of the same class). Make it easy to switch between these two views using a tab bar, page view controller or something similar. Copy the shader you built above, but remove the texture so that you just have a red ball against a gray background.

- 1. Make the sphere appear to have an alpha of 0.6. That is, make it partly transparent so that any background shows through.
- 2. Render a sphere that appears to be opaque and behind the first, red sphere. Make the second one cyan (#00ffff) with a radius much smaller than the first.
- 3. Make the second sphere orbit behind the first so that its center is always on the circumference of the original circle. Half the second sphere should show through from behind the first.
- 4. (Bonus) If there are jagged edges, smooth them out. (The image on the left has jagged edges. The one on the right includes some antialiasing to smooth them out.)

## Bonus:

1. Externalize the following parameters from the shaders:
 - Main sphere radius
 - Main sphere color
 - Animation speed

2. Add a pinch gesture recognizer to resize the sphere dynamically at runtime.

## *Some notes about the final result:*

- There is no multisampling enabled in the Android version and context creation is done using their libraries
- The drawables load their resources individually.  This is not the most efficient and could be redone using managers for resources like models, textures, shaders, etc.
- I used the math libraries from Android for matrices.  Ideally I could use a C++ library and the NDK for cross platform compiles
- Android version uses ETC compression!
- It was interesting porting over to Android but what I'd really like to do is investigate the NDK and cross platform usage of C++ libs

